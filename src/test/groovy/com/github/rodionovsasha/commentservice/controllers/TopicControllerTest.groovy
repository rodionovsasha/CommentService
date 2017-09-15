package com.github.rodionovsasha.commentservice.controllers

import spock.lang.Specification

class TopicControllerTest extends Specification {
    /*def service = Mock(TopicServiceImpl)
    def controller = new TopicController(service)
    def topic = new Topic()
    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def setup() {
        topic.name = "Topic name"
    }

    def "should get topic"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/topic/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.getTopicById(1) >> topic

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString.contains("\"id\":0,\"name\":\"Topic name\"")
    }

    def "should not get topic if not exists"() {
        given:
        service.getTopicById(1) >> {topic -> throw new TopicNotFoundException("Not found")}

        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/topic/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should not get topic if id is not correct"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/topic/null").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == INTERNAL_SERVER_ERROR.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString.contains('{"responseCode":500,"statusMessage":"Failed to convert value of type \'java.lang.String\' to required type \'long\';')
    }

    def "should add a new topic"() {
        when:
        def newTopic = '{"id":1,"name":"Topic name"}'
        def response = mockMvc.perform(post(API_BASE_URL + "/topic").contentType(APPLICATION_JSON_VALUE).content(newTopic)).andReturn().response

        then:
        1 * service.addTopic(_) >> topic

        response.status == CREATED.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
    }

    def "should not add a new topic when name is empty"() {
        given:
        topic.name = ""

        when:
        def newTopic = '{"id":1,"name":""}'
        def response = mockMvc.perform(post(API_BASE_URL + "/topic").contentType(APPLICATION_JSON_VALUE).content(newTopic)).andReturn().response

        then:
        0 * service.addTopic(_) >> topic

        response.status == BAD_REQUEST.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":400,"statusMessage":"name: may not be empty."}'
    }

    def "should update current topic"() {
        when:
        def updateTopic = '{"id":1,"name":"Topic name"}'
        def response = mockMvc.perform(put(API_BASE_URL + "/topic").contentType(APPLICATION_JSON_VALUE).content(updateTopic)).andReturn().response

        then:
        1 * service.updateTopic(_) >> topic

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
    }

    def "should not update topic"() {
        given:
        service.updateTopic(_) >> {topic -> throw new TopicNotFoundException("Not found")}

        when:
        def updateTopic = '{"id":1,"name":"Topic name"}'
        def response = mockMvc.perform(put(API_BASE_URL + "/topic").contentType(APPLICATION_JSON_VALUE).content(updateTopic)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should delete topic"() {
        when:
        def response = mockMvc.perform(delete(API_BASE_URL + "/topic/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.deleteTopic(1)

        response.status == NO_CONTENT.value()
    }*/
}
