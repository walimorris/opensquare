package com.morris.opensquare.services.impl;

import com.morris.opensquare.services.TextAnalyzerService;
import com.morris.opensquare.services.loggers.LoggerService;
import com.morris.opensquare.utils.Constants;
import com.morris.opensquare.utils.ExternalServiceUtil;
import com.twitter.twittertext.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.comprehend.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.morris.opensquare.utils.Constants.REGEX_STRICT_STRING_NUMBERS;

@Service
public class TextAnalyzerServiceImpl implements TextAnalyzerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextAnalyzerServiceImpl.class);

    private final ExternalServiceUtil externalServiceUtil;
    private final LoggerService loggerService;

    @Autowired
    public TextAnalyzerServiceImpl(ExternalServiceUtil externalServiceUtil, LoggerService loggerService) {
        this.externalServiceUtil = externalServiceUtil;
        this.loggerService = loggerService;
    }

    @Override
    public String getSentiment(SentimentScore score) {
        float positive = score.positive();
        float negative = score.negative();
        float neutral = score.neutral();

        String winner;
        float winnerScore;

        if (positive > negative) {
            winner = Constants.POSITIVE;
            winnerScore = positive;
        } else {
            winner = Constants.NEGATIVE;
            winnerScore = negative;
        }
        return neutral > winnerScore ? Constants.NEUTRAL : winner;
    }

    @Override
    public String analyzeCommentSentiment(String text, String region) {
        ComprehendClient comprehendClient = externalServiceUtil.getComprehendClient(region);
        String sentimentScore = null;
        try {
            DetectSentimentRequest sentimentRequest = DetectSentimentRequest.builder()
                    .text(text)
                    .languageCode("en")
                    .build();

            DetectSentimentResponse sentimentResponse = comprehendClient.detectSentiment(sentimentRequest);
            sentimentScore = getSentiment(sentimentResponse.sentimentScore());
        } catch (ComprehendException e) {
            loggerService.saveLog(e.getClass().getName(), "Exception in Comprehend Readings " + e.getMessage(), Optional.of(LOGGER));
        }
        comprehendClient.close();
        return sentimentScore;
    }

    @Override
    public List<KeyPhrase> detectKeyPhrasesFromYouTubeComment(String comment, String region) {
        ComprehendClient comprehendClient = externalServiceUtil.getComprehendClient(region);
        List<KeyPhrase> keyPhrases;
        try {
            DetectKeyPhrasesRequest keyPhrasesRequest = DetectKeyPhrasesRequest.builder()
                    .languageCode("en")
                    .text(comment)
                    .build();
            DetectKeyPhrasesResponse keyPhrasesResponse = comprehendClient.detectKeyPhrases(keyPhrasesRequest);
            keyPhrases = keyPhrasesResponse.keyPhrases();
            return keyPhrases;
        } catch (ComprehendException e) {
            loggerService.saveLog(e.getClass().getName(), "Error producing YouTube Comment keyPhrases: " + e.getMessage(), Optional.of(LOGGER));
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> extractUrlsFromText(String text) {
        Extractor urlExtractor = new Extractor();
        return urlExtractor.extractURLs(text);
    }

    @Override
    public boolean isValidYoutubeVideoId(String videoId) {
        Pattern pattern = Pattern.compile(REGEX_STRICT_STRING_NUMBERS, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoId);
        return matcher.find();
    }
}
