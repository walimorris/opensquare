package com.morris.opensquare.models.ai;

import lombok.*;

import java.util.Map;

/**
 * Vision Pulse is intended to be used with OpenAI's Vision API to understand images. Image prompts
 * to Vision API should contain text, an imageUrl and optional metadata. This metadata can/should be
 * used as extra context for OpenAI's model; to perform more detailed VisionPulses, i.e. adding
 * geolocation or metadata from an actual image for fine-tuned detail (or the most possible detail).
 * <br><br>
 * As OpenAi's Vision API improves, more capabilities will be added to Opensquare's VisionPulse.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisionPulse {
    private String text;
    private String imageUrl;
    private Map<String, Object> metaData;
}
