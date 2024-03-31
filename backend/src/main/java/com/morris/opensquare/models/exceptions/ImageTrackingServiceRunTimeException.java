package com.morris.opensquare.models.exceptions;

public class ImageTrackingServiceRunTimeException extends RuntimeException {
    public ImageTrackingServiceRunTimeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
