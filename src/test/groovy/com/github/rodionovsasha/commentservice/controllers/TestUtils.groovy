package com.github.rodionovsasha.commentservice.controllers

import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE

class TestUtils {
    static Object getJsonFromString(String content) {
        new JsonSlurper().parseText(content)
    }

    static def extractJson(response, status = HttpStatus.OK) {
        assert response.status == status.value()
        assert response.contentType == APPLICATION_JSON_UTF8_VALUE
        getJsonFromString(response.contentAsString)
    }
}
