package com.github.rodionovsasha.commentservice.controllers

import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class TestUtils {
    static JacksonJsonHttpMessageConverter jsonConverter() {
        new JacksonJsonHttpMessageConverter(JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build())
    }

    static Object getJsonFromString(String content) {
        new JsonSlurper().parseText(content)
    }

    static def extractJson(response, status = HttpStatus.OK) {
        assert response.status == status.value()
        assert response.contentType == APPLICATION_JSON_VALUE
        getJsonFromString(response.contentAsString)
    }
}
