package com.morris.opensquare.models.exceptions;

import java.io.IOException;

/**
 * OpenSent utilizes OpenSearch to load and index data. When errors occur on this load process,
 * this exception notifies the client of such exceptions in order to perform some deeper inspection
 * of OpenSearch or the data being loaded. Review the methods associated with index load.
 *
 * @see IOException
 */
public class OpenSquareYouTubeIndexLoadException extends RuntimeException {
    public OpenSquareYouTubeIndexLoadException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
