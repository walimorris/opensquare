package com.morris.opensquare.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.morris.opensquare.models.exceptions.PythonScriptEngineRunTimeException;
import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;
import com.morris.opensquare.models.youtube.YouTubeTranscribeSegment;
import com.morris.opensquare.services.loggers.LoggerService;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
@Component("pythonScriptEngine")
public class PythonScriptEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonScriptEngine.class);

    private static final String PYTHON = "python3";
    private static final String PYTHON_RESOURCE_PATH = "backend/src/main/resources/python/";
    private static final String TEMP_VIDEO_PATH = "backend/src/main/resources/videos/";
    private static final String YOUTUBE_TRANSCRIBE_SCRIPT = "transcribe_youtube_video.py";
    private static final String OPENAI_TEXT_EMBEDDING_ADA_002_SCRIPT = "text_embedding_ada_002.py";
    private static final String YOUTUBE_RAG_CHAIN_SCRIPT = "chatbot/youtube_vector_store_rag_chain.py";
    private final LoggerService loggerService;

    @Autowired
    private PythonScriptEngine(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public String processYouTubeRAGChain(@NonNull YouTubeRagChainProperties properties) {
        LOGGER.info("Hit PythonScriptEngine: YouTubeRAGChain");
        String resolvedScript = resolvePythonScriptPath(YOUTUBE_RAG_CHAIN_SCRIPT);
        List<String> result;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    PYTHON, resolvedScript,
                    properties.getMongodbUri(),
                    properties.getOpenaiKey(),
                    properties.getPrompt()
            );
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            result = readProcessOutput(process.getInputStream());
            boolean isError = thrownPossibleErrorAndPrintStatements(result);

            if (isError) {
                return null;
            }
            return String.join("", result);
        } catch (IOException e) {
            loggerService.saveLog(
                    e.getClass().getName(),
                    "Script Engine Error with file[" + resolvedScript + "]: " + e.getMessage(),
                    Optional.of(LOGGER)
            );
        }
        return null;
    }

    public List<Double> processPythonOpenAiAda002TextEmbeddingScript(String key, String text) {
        LOGGER.info("Hit PythonScriptEngine: Text-Embedding-ADA-002");
        String resolvedPytonFile = resolvePythonScriptPath(OPENAI_TEXT_EMBEDDING_ADA_002_SCRIPT);
        List<String> results = null;
        if (key != null && text != null) {
            try {
                results = getStrings(key, resolvedPytonFile, text);
                if (results == null) return new ArrayList<>();

            } catch (IOException e) {
                loggerService.saveLog(
                        e.getClass().getName(),
                        "Script Engine Error with file[" + resolvedPytonFile + "]: " + e.getMessage(),
                        Optional.of(LOGGER)
                );
            }
        }
        return results != null ? getAda002Embeddings(results) : null;
    }

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
            results = getStrings(urlArg, resolvedPytonFile, resolvedVideoTempPath);
            if (results == null) return new ArrayList<>();
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

    private List<Double> getAda002Embeddings(List<String> stream) {
        List<Double> embeddingResults = new ArrayList<>();
        String embeddingStr = null;
        for (String line: stream) {
            LOGGER.info("{}", line);
            if (line.charAt(0) == '[') {
                embeddingStr = line;
            }
        }
        if (embeddingStr != null) {
            embeddingStr = embeddingStr
                    .replace("[", "")
                    .replace("]", "").trim();

            List<String> embeddingsWithStrNumbers = new ArrayList<>(Arrays.asList(embeddingStr.split(",")));
            if (!embeddingsWithStrNumbers.isEmpty()) {
                embeddingsWithStrNumbers.forEach(strNumber -> embeddingResults.add(Double.valueOf(strNumber)));
            }
        }
        return embeddingResults;
    }

    @Nullable
    private List<String> getStrings(String urlArg, String resolvedPytonFile, String resolvedVideoTempPath) throws IOException {
        List<String> results;
        ProcessBuilder processBuilder = new ProcessBuilder(PYTHON, resolvedPytonFile, urlArg, resolvedVideoTempPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        results = readProcessOutput(process.getInputStream());
        boolean isError = thrownPossibleErrorAndPrintStatements(results);
        if (isError) {
            return null;
        }
        return results;
    }

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

    private boolean thrownPossibleErrorAndPrintStatements(List<String> pythonOutputStream) {
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
        return error;
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
