package com.morris.opensquare.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface ObjectMapperService {

    /**
     * Write Java {@link Object} pojo as json string.
     *
     * @param pojo {@link Object}
     *
     * @see ObjectMapper#writeValueAsString(Object)
     *
     * @return {@link String}
     * @throws JsonProcessingException
     */
    String writeObjectAsString(Object pojo) throws JsonProcessingException;
}
