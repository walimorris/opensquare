package com.morris.opensquare.models.exceptions;

public class OpenSquareMultiPartFileException extends RuntimeException {
    public OpenSquareMultiPartFileException (String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
