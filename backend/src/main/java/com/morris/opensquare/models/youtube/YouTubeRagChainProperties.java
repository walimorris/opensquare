package com.morris.opensquare.models.youtube;

import dev.langchain4j.store.embedding.mongodb.MongoDbEmbeddingStore;

/**
 * YouTubeRagChainProperties object carries various properties required to create
 * a RAG chain prompt response. MongoDB vector storage is used as the RAG chain
 * persistence store which requires the secured database uri. Openai is used to
 * create the RAG chain using the text-embedding-ada-002 model for the original
 * and prompt response.
 */
public class YouTubeRagChainProperties {
    private String mongodbUri;
    private String openaiKey;
    private MongoDbEmbeddingStore vectorStore;
    private String prompt;
    private String system;

    public YouTubeRagChainProperties() {}

    public YouTubeRagChainProperties(Builder builder) {
        this.mongodbUri = builder.mongodbUri;
        this.openaiKey = builder.openaiKey;
        this.vectorStore = builder.vectorStore;
        this.prompt = builder.prompt;
        this.system = builder.system;
    }

    public String getMongodbUri() {
        return mongodbUri;
    }

    public void setMongodbUri(String mongodbUri) {
        this.mongodbUri = mongodbUri;
    }

    public String getOpenaiKey() {
        return openaiKey;
    }

    public void setOpenaiKey(String openaiKey) {
        this.openaiKey = openaiKey;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public MongoDbEmbeddingStore getVectorStore() {
        return vectorStore;
    }

    public void setVectorStore(MongoDbEmbeddingStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public static class Builder {
        private String mongodbUri;
        private String openaiKey;
        private String prompt;
        private MongoDbEmbeddingStore vectorStore;
        private String system;

        public Builder() {
            // nothing to complete inside Builder
        }

        public Builder mongodbUri(String mongodbUri) {
            this.mongodbUri = mongodbUri;
            return this;
        }

        public Builder openaiKey(String openaiKey) {
            this.openaiKey = openaiKey;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder vectorStore(MongoDbEmbeddingStore vectorStore) {
            this.vectorStore = vectorStore;
            return this;
        }

        public Builder system(String system) {
            this.system = system;
            return this;
        }

        public YouTubeRagChainProperties build() {
            return new YouTubeRagChainProperties(this);
        }
    }
}
