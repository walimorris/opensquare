package com.morris.opensquare.services;

import com.morris.opensquare.utils.Constants;
import software.amazon.awssdk.services.comprehend.model.KeyPhrase;
import software.amazon.awssdk.services.comprehend.model.SentimentScore;

import java.util.List;

public interface TextAnalyzerService {

    /**
     * Get sentiment of text based on the highest sentiment score comparing
     * negative, positive, and neutral sentiment scores.
     *
     * @param score {@link SentimentScore}
     *
     * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-sentiment.html">Sentiment Score Docs</a>
     *
     * @return {@link String} sentiment score one of POSITIVE, NEGATIVE, NEUTRAL
     */
    String getSentiment(SentimentScore score);

    /**
     * @param text {@link String}
     * @param region {@link String} aws region for service
     *
     * @see <a href="https://docs.aws.amazon.com/comprehend/latest/dg/how-sentiment.html">Sentiment Score Docs</a>
     *
     * @return {@link String}
     */
    String analyzeCommentSentiment(String text, String region);

    /**
     * Extracts urls from given text content.
     *
     * @param text {@link String}
     *
     * @return {@link List<String>}
     */
    List<String> extractUrlsFromText(String text);

    /**
     * Checks if YouTube video id is valid. Valid YouTube video ids contain numerical
     * and alphabetical characters only. Any videoId that contains anything else is
     * invalid.<br><br>
     * REGEX: ^[A-Za-z0-9]*$ <br><br>
     * ^ - matches beginning <br><br>
     * $ - matches end <br><br>
     * [] - character class match 3 ranges A-Z, a-z, 0-9 <br><br>
     * * - matches preceding item zero or more times <br><br>
     *
     * @param videoId {@link String} youtube videoId.
     *
     * @see Constants
     * @return boolean
     */
    boolean isValidYoutubeVideoId(String videoId);

    /**
     * Detect KeyPhrases in YouTube comment.
     *
     * @param comment {@link String} YouTube Comment
     * @param region {@link String} service region
     *
     * @return {@link List<KeyPhrase>}
     */
    List<KeyPhrase> detectKeyPhrasesFromYouTubeComment(String comment, String region);
}
