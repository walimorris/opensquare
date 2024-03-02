package com.morris.opensquare.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morris.opensquare.models.exceptions.OpenSquareYouTubePropertiesException;
import com.morris.opensquare.services.SecretsService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;

@Service
public class SecretsServiceImpl implements SecretsService {

    @Override
    public Object getSecret(SecretsManagerClient secretsManagerClient, String secretName, Class<?> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        String secretValueResponseString;

        try {
            secretValueResponseString = secretsManagerClient.getSecretValue(getSecretValueRequest).secretString();
        } catch (SecretsManagerException e) {
            throw new OpenSquareYouTubePropertiesException("Error receiving OpenSent Youtube Service secret properties", e);
        }
        try {
            return mapper.readValue(secretValueResponseString, clazz);
        } catch (JsonProcessingException e) {
            throw new OpenSquareYouTubePropertiesException("Error marshalling OpenSent YouTube secret properties to OpenSentYouTubeSecret: ", e);
        }
    }
}
