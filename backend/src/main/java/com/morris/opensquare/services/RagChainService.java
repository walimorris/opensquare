package com.morris.opensquare.services;

public interface RagChainService {

    /**
     * Get prompt response using langchain openai.
     * @see <a href="https://python.langchain.com/docs/integrations/vectorstores/mongodb_atlas">Langchain Integration Docs</a>
     *
     * @param prompt {@link String} user prompt
     *
     * @return {@link String} prompt response
     */
    String promptResponse(String prompt);
}
