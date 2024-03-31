package com.morris.opensquare.services.impl;

import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;
import com.morris.opensquare.services.RagChainService;
import com.morris.opensquare.utils.PythonScriptEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YouTubeRagChainServiceImpl implements RagChainService {
    private final PythonScriptEngine pythonScriptEngine;
    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    public YouTubeRagChainServiceImpl(PythonScriptEngine pythonScriptEngine, ApplicationPropertiesConfiguration applicationPropertiesConfiguration) {
        this.pythonScriptEngine = pythonScriptEngine;
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
    }

    @Override
    public String promptResponse(String prompt) {
        return pythonScriptEngine.processYouTubeRAGChain(
                new YouTubeRagChainProperties.Builder()
                .mongodbUri(applicationPropertiesConfiguration.mongodbUri())
                .openaiKey(applicationPropertiesConfiguration.openAI())
                .prompt(prompt)
                .build()
        );
    }
}
