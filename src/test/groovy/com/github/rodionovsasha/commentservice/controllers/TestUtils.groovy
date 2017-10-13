package com.github.rodionovsasha.commentservice.controllers

import groovy.json.JsonSlurper

class TestUtils {
    static Object getJsonFromString(String content) {
        return new JsonSlurper().parseText(content)
    }
}
