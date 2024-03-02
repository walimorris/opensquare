package com.morris.opensquare.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morris.opensquare.services.ObjectMapperService;
import org.springframework.stereotype.Service;

@Service
public class ObjectMapperServiceImpl implements ObjectMapperService {

    public String writeObjectAsString(Object pojo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(pojo);
    }
}
