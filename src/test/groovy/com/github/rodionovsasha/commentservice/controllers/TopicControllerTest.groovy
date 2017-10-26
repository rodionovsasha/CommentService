package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.TopicAccessException
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.services.TopicService
import groovy.json.JsonOutput
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static com.github.rodionovsasha.commentservice.controllers.TestUtils.getJsonFromString
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE

class TopicControllerTest extends Specification {
    final TOPIC_ID = 1
    final USER_ID = 1
    final NOT_EXISTING_TOPIC_ID = 99
    final TOPIC_TITLE = "Stupid Flanders"

    def service = Mock(TopicService)
    def controller = new TopicController(service)
    def user = Mock(User)
    def topic = new Topic(TOPIC_TITLE, user)

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "#getById returns topic by id"() {
        when:
        def response = getById(TOPIC_ID)

        then:
        1 * service.getById(TOPIC_ID) >> topic
        with(response) {
            status == HttpStatus.OK.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            def responseJson = getJsonFromString(contentAsString)
            responseJson.id == 0
            responseJson.title == TOPIC_TITLE
            !responseJson.archived
            responseJson.date instanceof Long
        }
    }

    def "#getById does not return topic when not exists"() {
        given:
        service.getById(NOT_EXISTING_TOPIC_ID) >> {throw TopicNotFoundException.forId(NOT_EXISTING_TOPIC_ID)}

        when:
        def response = getById(NOT_EXISTING_TOPIC_ID)

        then:
        with(response) {
            status == HttpStatus.NOT_FOUND.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:404, message: "The topic with id '99' could not be found"]
        }
    }

    def "#start creates a new topic"() {
        when:
        def response = startTopic([title: TOPIC_TITLE], USER_ID)

        then:
        1 * service.start(TOPIC_TITLE, USER_ID) >> topic
        with(response) {
            status == HttpStatus.CREATED.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            def responseJson = getJsonFromString(contentAsString)
            responseJson.id == 0
            responseJson.title == TOPIC_TITLE
            !responseJson.archived
            responseJson.date instanceof Long
        }
    }

    def "#start does not create a new topic when user not exists"() {
        given:
        service.start(TOPIC_TITLE, USER_ID) >> {throw UserNotFoundException.forId(USER_ID)}

        when:
        def response = startTopic([title: TOPIC_TITLE], USER_ID)

        then:
        with(response) {
            status == HttpStatus.NOT_FOUND.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code: 404, message: "The user with id '1' could not be found"]
        }
    }

    def "#start does not create a new topic when user is not active"() {
        given:
        service.start(TOPIC_TITLE, USER_ID) >> {throw InactiveUserException.forId(USER_ID)}

        when:
        def response = startTopic([title: TOPIC_TITLE], USER_ID)

        then:
        with(response) {
            status == HttpStatus.FORBIDDEN.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code: 403, message: "The user with id '1' is not active"]
        }
    }

    def "#start does not create a new topic when title is empty"() {
        when:
        def response = startTopic([title: ""], USER_ID)

        then:
        0 * service.start("", USER_ID)
        with(response) {
            status == HttpStatus.INTERNAL_SERVER_ERROR.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            def responseJson = getJsonFromString(contentAsString)
            responseJson.code == 500
            responseJson.message.contains("may not be empty")
        }
    }

    def "#archive archives topic"() {
        when:
        def response = archive(TOPIC_ID, USER_ID)

        then:
        1 * service.archive(TOPIC_ID, USER_ID)
        response.status == HttpStatus.OK.value()
    }

    def "#archive throws when user is not active"() {
        given:
        service.archive(TOPIC_ID, USER_ID) >> { throw InactiveUserException.forId(USER_ID) }

        when:
        def response = archive(TOPIC_ID, USER_ID)

        then:
        with(response) {
            status == HttpStatus.FORBIDDEN.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code: 403, message: "The user with id '1' is not active"]
        }
    }

    def "#archive throws when user is not owner"() {
        given:
        service.archive(TOPIC_ID, USER_ID) >> { throw TopicAccessException.forId(USER_ID) }

        when:
        def response = archive(TOPIC_ID, USER_ID)

        then:
        with(response) {
            status == HttpStatus.FORBIDDEN.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code: 403, message: "Non-topic owner with id '1' is trying to archive the topic"]
        }
    }

    private MockHttpServletResponse getById(long id) {
        mockMvc.perform(get(API_BASE_URL + "/topic/" + id).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse startTopic(Map json, long id) {
        mockMvc.perform(post(API_BASE_URL + "/topic/user/" + id).contentType(APPLICATION_JSON_VALUE).content(JsonOutput.toJson(json)))
                .andReturn().response
    }

    private MockHttpServletResponse archive(long topicId, long userId) {
        mockMvc.perform(get(API_BASE_URL + "/topic/archive/" + topicId + "/user/" + userId).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }
}
