package com.morris.opensquare.services.impl;

import com.mongodb.client.MongoClient;
import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;
import com.morris.opensquare.services.RagChainService;
import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YouTubeRagChainServiceImpl implements RagChainService {
    private final OpenAiServiceImpl openAiService;
    private final MongoClient mongoClient;
    private final ApplicationPropertiesConfiguration applicationPropertiesConfiguration;

    @Autowired
    public YouTubeRagChainServiceImpl(OpenAiServiceImpl openAiService, ApplicationPropertiesConfiguration applicationPropertiesConfiguration,
                                      MongoClient mongoClient, YouTubeServiceImpl youTubeService) {
        this.openAiService = openAiService;
        this.applicationPropertiesConfiguration = applicationPropertiesConfiguration;
        this.mongoClient = mongoClient;
    }

    @Override
    public String promptResponse(String prompt, int id) {
        MongoDbEmbeddingStore embeddingStore = MongoDbEmbeddingStore.builder()
                .fromClient(mongoClient)
                .databaseName(applicationPropertiesConfiguration.database())
                .collectionName(applicationPropertiesConfiguration.youtubeCollection())
                .indexName(applicationPropertiesConfiguration.youtubeVectorIndex())
                .build();

        return openAiService.processYouTubeRAGChain(
                YouTubeRagChainProperties.builder()
                        .mongodbUri(applicationPropertiesConfiguration.mongodbUri())
                        .openaiKey(applicationPropertiesConfiguration.openAI())
                        .vectorStore(embeddingStore)
                        .prompt(prompt)
                        .memoryId(id)
                        .build()
        );
    }
}
