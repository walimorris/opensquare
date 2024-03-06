package com.morris.opensquare.models.exceptions;

public class PythonScriptEngineRunTimeException extends RuntimeException {
    public PythonScriptEngineRunTimeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
