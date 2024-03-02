package com.morris.opensquare.services;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

public interface SecretsService {

    /**
     * Gets Secrets from Amazon Secrets manager.
     *
     * @param secretsManagerClient {@link SecretsManagerClient}
     * @param secretName {@link String} Secrets manager secret name
     * @param clazz class should provide fields equivalent to the properties contained in secret value
     * @apiNote clazz parameter should be refactored to extend a base Secrets class.
     *
     * @return {@link Object}
     *
     * @see <a href="https://docs.aws.amazon.com/secretsmanager/latest/userguide/intro.html">Secrets Manager Docs</a>
     */
    Object getSecret(SecretsManagerClient secretsManagerClient, String secretName, Class<?> clazz);
}
