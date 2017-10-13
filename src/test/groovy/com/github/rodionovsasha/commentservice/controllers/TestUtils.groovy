package com.github.rodionovsasha.commentservice.controllers

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class TestUtils {
    static Object getJsonFromString(String content) {
        new JsonSlurper().parseText(content)
    }

    static String getJsonStringFromObject(Map content) {
        JsonOutput.toJson(content)
    }
}
