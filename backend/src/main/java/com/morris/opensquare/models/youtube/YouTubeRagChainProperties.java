package com.morris.opensquare.models.youtube;

import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;
import lombok.*;

/**
 * YouTubeRagChainProperties object carries various properties required to create
 * a RAG chain prompt response. MongoDB vector storage is used as the RAG chain
 * persistence store which requires the secured database uri. Openai is used to
 * create the RAG chain using the text-embedding-ada-002 model for the original
 * and prompt response.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YouTubeRagChainProperties {

    /* MongoDB URI - needed to reference vector embedding stores */
    private String mongodbUri;

    /* OpenAI API Key */
    private String openaiKey;

    /* MongoDbEmbeddingStore provides properties for MongoDB connection and configurations */
    private MongoDbEmbeddingStore vectorStore;

    /* user message */
    private String prompt;

    /* unique id for chat memory */
    private int memoryId;
}
