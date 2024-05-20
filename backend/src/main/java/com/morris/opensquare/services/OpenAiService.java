package com.morris.opensquare.services;

import com.morris.opensquare.models.trackers.VisionPulse;
import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;

import java.util.List;

public interface OpenAiService {

    /**
     * Prompt OpenAI Language Model and completes a Retrieval Augmented Generation (RAG)
     * response, based on given context.
     *
     * @param youTubeRagChainProperties {@link YouTubeRagChainProperties}
     *
     * @return {@link String} AI Response
     */
    String processYouTubeRAGChain(YouTubeRagChainProperties youTubeRagChainProperties);

    /**
     * Convert given text into vector embeddings representation.
     *
     * @param key {@link String} OpenAI API key
     * @param text {@link String} text to embed
     *
     * @return {@link List<Float>} embeddings
     */
    List<Float> processOpenAiAda002TextEmbedding(String key, String text);

    /**
     * Process a Vision request to OpenAI model, requesting information about given image with possible
     * metadata as extra context for more accurate or detailed answers.
     *
     * @param visionPulse {@link VisionPulse} contains text, image, and image metadata
     *
     * @return {@link String} answer
     */
    String processVisionPulse(VisionPulse visionPulse);
}
