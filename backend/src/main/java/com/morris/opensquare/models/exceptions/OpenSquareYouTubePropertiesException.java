package com.morris.opensquare.models.exceptions;

public class OpenSquareYouTubePropertiesException extends RuntimeException {
    public OpenSquareYouTubePropertiesException(String errorMessage) {
        super(errorMessage);
    }

    public OpenSquareYouTubePropertiesException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
