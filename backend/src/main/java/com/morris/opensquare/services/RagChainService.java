package com.morris.opensquare.services;

public interface RagChainService {

    /**
     * Generate prompt response with given user message prompt.
     *
     * @param prompt {@link String} user message
     *
     * @return {@link String} prompt response
     */
    String promptResponse(String prompt);
}
