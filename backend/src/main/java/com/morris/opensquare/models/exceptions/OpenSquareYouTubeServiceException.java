package com.morris.opensquare.models.exceptions;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * OpenSent utilizes the {@link YouTube} google api services. Therefore, OpenSent
 * must build the service using http transport protocols. These protocol exceptions should be caught during the
 * YouTube service creation time (see thrown exceptions below). If these exceptions are uncaught, we know that
 * there was some other error from connection or http transport issues that must be logged and checked.
 *
 * @see GoogleNetHttpTransport
 * @see GeneralSecurityException
 * @see IOException
 * @see RuntimeException
 *
 */
public class OpenSquareYouTubeServiceException extends RuntimeException {
    public OpenSquareYouTubeServiceException(String errorMessage) {
        super(errorMessage);
    }
}
