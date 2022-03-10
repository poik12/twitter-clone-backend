package com.jd.twitterclonebackend.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonMapper {

    public <T> T mapFromJsonToDto(String jsonRequest, Class<T> requestDtoClass) {
        T requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(
                    jsonRequest,
                    requestDtoClass
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestDto;
    }

}
