package com.morris.opensquare.services;

import com.morris.opensquare.models.ai.VisionPulse;
import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;

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
     * Process a Vision request to OpenAI model, requesting information about given image with possible
     * metadata as extra context for more accurate or detailed answers.
     *
     * @param visionPulse {@link VisionPulse} contains text, image, and image metadata
     *
     * @return {@link String} answer
     */
    String processVisionPulse(VisionPulse visionPulse);

    /**
     * Generates an image given properties of {@link VisionPulse}. Generated images can be
     * based on a given image and prompt or generated from a single prompt.
     * <br><br>
     * Dall-e-2 feature of passing images for edit is not currently available in langchain4j.
     * As soon as this is implemented this feature will be available.
     *
     * @param visionPulse {@link VisionPulse}
     *
     * @return {@link String} Base64Encoded String
     */
    public String generateImage(VisionPulse visionPulse);
}
