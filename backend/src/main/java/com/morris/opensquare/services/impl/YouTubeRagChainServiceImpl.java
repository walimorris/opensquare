package com.morris.opensquare.services.impl;

import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.services.RagChainService;
import com.morris.opensquare.services.loggers.LoggerService;
import com.morris.opensquare.utils.PythonScriptEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YouTubeRagChainServiceImpl implements RagChainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(YouTubeRagChainServiceImpl.class);

    private final LoggerService loggerService;
    private final PythonScriptEngine pythonScriptEngine;
    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    public YouTubeRagChainServiceImpl(LoggerService loggerService, PythonScriptEngine pythonScriptEngine,
                                      ApplicationPropertiesConfiguration applicationPropertiesConfiguration) {
        this.loggerService = loggerService;
        this.pythonScriptEngine = pythonScriptEngine;
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
    }

    @Override
    public String promptResponse(String prompt) {
        String mongodbUri = applicationPropertiesConfiguration.mongodbUri();
        String openaiKey = applicationPropertiesConfiguration.openAI();
        return pythonScriptEngine.processYouTubeRAGChain(mongodbUri, openaiKey, prompt);
    }
}
