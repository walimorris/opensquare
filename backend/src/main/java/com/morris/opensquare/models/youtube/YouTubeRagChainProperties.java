package com.morris.opensquare.models.youtube;

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
    private String prompt;

    public YouTubeRagChainProperties() {}

    public YouTubeRagChainProperties(Builder builder) {
        this.mongodbUri = builder.mongodbUri;
        this.openaiKey = builder.openaiKey;
        this.prompt = builder.prompt;
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

    public static class Builder {
        private String mongodbUri;
        private String openaiKey;
        private String prompt;

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

        public YouTubeRagChainProperties build() {
            return new YouTubeRagChainProperties(this);
        }
    }
}
