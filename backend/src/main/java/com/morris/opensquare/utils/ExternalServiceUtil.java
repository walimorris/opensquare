package com.morris.opensquare.utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.morris.opensquare.models.exceptions.OpenSquareYouTubeServiceException;
import com.morris.opensquare.services.loggers.LoggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.SdkClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.comprehend.ComprehendClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import static com.morris.opensquare.utils.Constants.*;

@Component("externalServiceUtil")
public class ExternalServiceUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalServiceUtil.class);

    private final LoggerService loggerService;

    @Autowired
    private ExternalServiceUtil(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    /**
     * Stops (sleeps) thread for opensent system processing.
     *
     * @param possibleErrorMessage {@link String} error message in case of error
     * @param time thread pause time
     */
    public void sleep(String possibleErrorMessage, long time) {
        StringBuilder err = new StringBuilder(possibleErrorMessage + ": {}");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            loggerService.saveLog(e.getClass().getName(), e.getMessage(), Optional.of(LOGGER));
            Thread.currentThread().interrupt();
        }
    }

    public Customsearch getGoogleCustomSearchService(String applicationName, String apiKey) {
        Customsearch customsearch = null;
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            customsearch = new Customsearch.Builder(httpTransport, JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName(applicationName)
                    .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(apiKey))
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            loggerService.saveLog(e.getClass().getName(), ERROR_GOOGLE_CUSTOM_SEARCH_SERVICE + ": " + e.getMessage(), Optional.of(LOGGER));
        }
        return customsearch;
    }

    /**
     * Gets YouTube API Service for requests.
     *
     * @param applicationName name of YouTube-based application
     *
     * @return {@link YouTube}
     */
    public YouTube getYouTubeService(String applicationName) {
        YouTube youTube = null;
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            youTube = new YouTube.Builder(httpTransport, JacksonFactory.getDefaultInstance(), null)
                    .setApplicationName(applicationName)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            loggerService.saveLog(e.getClass().getName(), ERROR_YOUTUBE_SERVICE_2 + " " + applicationName + ": " + e.getMessage(), Optional.of(LOGGER));
        }
        if (youTube == null) {
            loggerService.saveLog(OpenSquareYouTubeServiceException.class.getName(), ERROR_YOUTUBE_SERVICE_1, Optional.empty());
            throw new OpenSquareYouTubeServiceException(ERROR_YOUTUBE_SERVICE_1);
        }
        return youTube;
    }

    /**
     * Get Amazon Secrets Manager Client.
     *
     * @return {@link SecretsManagerClient}
     */
    public SecretsManagerClient getSecretsManagerClient(String region) {
        return SecretsManagerClient.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    /**
     * Get Amazon Comprehend Client.
     *
     * @return {@link ComprehendClient}
     */
    public ComprehendClient getComprehendClient(String region) {
        return ComprehendClient.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    /**
     * Get Amazon S3 Client.
     *
     * @return {@link S3Client}
     */
    public S3Client getS3Client(String region) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    /**
     * Closes {@link SdkClient} clients.
     *
     * @param clients clients
     */
    public void closeSdkClients(SdkClient... clients) {
        for (SdkClient client : clients) {
            if (client != null) {
                client.close();
            }
        }
    }
}
