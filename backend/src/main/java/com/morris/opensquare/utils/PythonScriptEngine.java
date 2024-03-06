package com.morris.opensquare.utils;

import com.morris.opensquare.services.loggers.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
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
    private final LoggerService loggerService;

    @Autowired
    private PythonScriptEngine(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public String processPython(String pyFile) {
        List<String> results = null;
        String resolvedPytonFile = resolvePythonScriptPath(pyFile);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(PYTHON, resolvedPytonFile);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            results = readProcessOutput(process.getInputStream());
            int i = 0;
            for (String result : results) {
                LOGGER.info("Line[{}]: {}", i, result);
                i++;
            }
        } catch (IOException e) {
            loggerService.saveLog(
                    e.getClass().getName(),
                    "Script Engine Error with file[" + resolvedPytonFile + "]: " + e.getMessage(),
                    Optional.of(LOGGER)
            );
        }
        return results != null ? results.get(0) : null;
    }

    private String resolvePythonScriptPath(String filename) {
        File file = new File(PYTHON_RESOURCE_PATH + filename);
        return file.getAbsolutePath();
    }

    private List<String> readProcessOutput(InputStream inputStream) throws IOException {
        try (BufferedReader output = new BufferedReader(new InputStreamReader(inputStream))) {
            return output.lines().collect(Collectors.toList());
        }
    }
}
