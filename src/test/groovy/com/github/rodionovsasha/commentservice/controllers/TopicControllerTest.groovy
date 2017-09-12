package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.services.TopicService
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class TopicControllerTest extends Specification {
    def service = Mock(TopicService)
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

    def "should not get topic"() {
        given:
        service.getTopicById(1) >> {topic -> throw new TopicNotFoundException("Not found")}

        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/topic/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
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
    }
}
