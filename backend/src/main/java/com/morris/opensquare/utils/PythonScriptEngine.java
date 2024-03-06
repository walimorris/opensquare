package com.morris.opensquare.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morris.opensquare.models.exceptions.PythonScriptEngineRunTimeException;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.services.loggers.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The PythonScriptEngine is a tiny engine for running python scripts on the Opensquare platform. There are a few
 * design decisions behind adding such an engine:
 * <br><br>
 *     1. Opensquare is a data platform for analytics and as such requires many of the powerful Python libraries<br>
 *     2. Many analytics workloads are better suited to Python and might be too bulky in Java or Kotlin<br>
 *     3. Newer AI libraries, like OpenAI or Whisper, doesn't provide simple Java binding and only accessible through Python
 * <br><br>
 * Python scripts should be stored in SpringBoot's 'resources' folder under 'python' directory. That is where this
 * script engine will look and resolve the full path. Scripts can provide output such as text that can be piped to API
 * responses or other helper functions in the wider platform. Some scripts can provide automation tasks or other
 * background processes.
 * <br><br>
 * The main driver of the PythonScriptEngine is Java's native {@link ProcessBuilder} which is used to create operating
 * system processes and therefore requires a running version of Python3. We keep a strict guideline of running processes
 * that won't have major side effects such as accessing/changing data while another process is attempting to access the
 * same data.
 */
@Component("PythonScriptEngine")
public class PythonScriptEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonScriptEngine.class);

    private static final String PYTHON = "python3";
    private static final String PYTHON_RESOURCE_PATH = "backend/src/main/resources/python/";
    private static final String TEMP_VIDEO_PATH = "backend/src/main/resources/videos/";
    private static final String YOUTUBE_TRANSCRIBE_SCRIPT = "transcribe_youtube_video.py";
    private final LoggerService loggerService;

    @Autowired
    private PythonScriptEngine(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    // TODO: Parse possible python errors and ensure those are propagated to Spring error handling
    public List<YouTubeTranscribeSegment> processPythonTranscribeScript(String url) {
        LOGGER.info("Hit PythonScriptEngine");
        List<String> results;
        List<YouTubeTranscribeSegment> output = null;
        String urlArg = "";
        String resolvedPytonFile = resolvePythonScriptPath(YOUTUBE_TRANSCRIBE_SCRIPT);
        String resolvedVideoTempPath = resolvePath(TEMP_VIDEO_PATH);

        if (url !=  null) {
            urlArg = url;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(PYTHON, resolvedPytonFile, urlArg, resolvedVideoTempPath);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            results = readProcessOutput(process.getInputStream());
            throwPossibleErrorAndPrintStatements(results);
            output = getSegments(results);

        } catch (IOException e) {
            loggerService.saveLog(
                    e.getClass().getName(),
                    "Script Engine Error with file[" + resolvedPytonFile + "]: " + e.getMessage(),
                    Optional.of(LOGGER)
            );
        }
        return output;
    }

    // {'time': 141.44, 'text': ' Thanks for watching, and I will see you in the next one.'}
    private List<YouTubeTranscribeSegment> getSegments(List<String> pythonOutputStream) {
        List<YouTubeTranscribeSegment> segments = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (String line : pythonOutputStream) {
            if (containsSegment(line)) {
                line = exchangeTranscribeSegmentQuotes(line);
                try {
                    YouTubeTranscribeSegment segment = mapper.readValue(line, YouTubeTranscribeSegment.class);
                    segment.setText(segment.getText().trim());
                    segments.add(segment);
                } catch (IOException e) {
                    loggerService.saveLog(
                            e.getClass().getName(),
                            "Python Script Engine Error: " + e.getMessage(),
                            Optional.of(LOGGER)
                    );
                }
            }
        }
        return segments;
    }

    private boolean containsSegment(String line) {
        return line.contains("time") && line.contains("text");
    }

    private String exchangeTranscribeSegmentQuotes(String line) {
        return line.replace("'time'", "\"time\"")
                .replace("'text'", "\"text\"")
                .replace("' ", "\"")
                .replace("'}", "\"}");
    }

    private void throwPossibleErrorAndPrintStatements(List<String> pythonOutputStream) {
        StringBuilder builder = new StringBuilder();
        boolean error = false;
        for (String line : pythonOutputStream) {
            builder.append(line).append("\n");
            if (line.contains("Traceback")) {
                error = true;
            }
        }
        if (error) {
            throw new PythonScriptEngineRunTimeException(builder.toString(), new IOException());
        }
    }

    private String resolvePythonScriptPath(String filename) {
        File file = new File(PYTHON_RESOURCE_PATH + filename);
        return file.getAbsolutePath();
    }

    private String resolvePath(String filename) {
        File file = new File(filename);
        return  file.getAbsolutePath();
    }

    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines().collect(Collectors.toList());
        }
    }
}
