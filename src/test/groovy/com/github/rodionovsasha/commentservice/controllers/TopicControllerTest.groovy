package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.*
import com.github.rodionovsasha.commentservice.services.TopicService
import groovy.json.JsonOutput
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static com.github.rodionovsasha.commentservice.controllers.TestUtils.extractJson
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
    def topic = new Topic("Stupid Flanders", user)
    def topics = [new Topic("Stupid Flanders", user),
                  new Topic("Ay Caramba!", user),
                  new Topic("Shut up Flanders!", user),
                  new Topic("Why you little...!", user)]

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "#getById returns topic by id"() {
        when:
        def response = extractJson(getById(TOPIC_ID))

        then:
        1 * service.getById(TOPIC_ID) >> topic
        with(response) {
            id == 0
            title == "Stupid Flanders"
            !archived
            date instanceof Long
        }
    }

    def "#getById does not return topic when not exists"() {
        given:
        service.getById(NOT_EXISTING_TOPIC_ID) >> { throw TopicNotFoundException.forId(NOT_EXISTING_TOPIC_ID) }

        when:
        def response = getById(NOT_EXISTING_TOPIC_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The topic with id '99' could not be found"]
    }

    def "#start creates a new topic"() {
        when:
        def response = extractJson(startTopic([title: TOPIC_TITLE], USER_ID), HttpStatus.CREATED)

        then:
        1 * service.start(TOPIC_TITLE, USER_ID) >> topic
        with(response) {
            id == 0
            title == "Stupid Flanders"
            !archived
            date instanceof Long
        }
    }

    def "#start does not create a new topic when user not exists"() {
        given:
        service.start(TOPIC_TITLE, USER_ID) >> { throw UserNotFoundException.forId(USER_ID) }

        when:
        def response = startTopic([title: TOPIC_TITLE], USER_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code:404, message: "The user with id '1' could not be found"]
    }

    def "#start does not create a new topic when user is not active"() {
        given:
        service.start(TOPIC_TITLE, USER_ID) >> { throw InactiveUserException.forId(USER_ID) }

        when:
        def response = startTopic([title: TOPIC_TITLE], USER_ID)

        then:
        extractJson(response, HttpStatus.FORBIDDEN) == [code: 403, message: "The user with id '1' is not active"]
    }

    def "#start does not create a new topic when title is empty"() {
        when:
        def response = extractJson(startTopic([title: ""], USER_ID), HttpStatus.INTERNAL_SERVER_ERROR)

        then:
        0 * service.start("", USER_ID)
        with(response) {
            code == 500
            message.contains("may not be empty")
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
        extractJson(response, HttpStatus.FORBIDDEN) == [code: 403, message: "The user with id '1' is not active"]
    }

    def "#archive throws when user is not owner"() {
        given:
        service.archive(TOPIC_ID, USER_ID) >> { throw TopicAccessException.forId(USER_ID) }

        when:
        def response = archive(TOPIC_ID, USER_ID)

        then:
        extractJson(response, HttpStatus.FORBIDDEN) == [code: 403, message: "Non-topic owner with id '1' is trying to archive the topic"]
    }

    def "#getActiveTopic returns active topic by id"() {
        when:
        def response = getActiveTopic(TOPIC_ID)

        then:
        1 * service.getActiveTopic(TOPIC_ID) >> topic
        !extractJson(response).archived
    }

    def "#getActiveTopic does not return topic when topic is archived"() {
        given:
        service.getActiveTopic(TOPIC_ID) >> { throw ArchivedTopicException.forId(TOPIC_ID) }

        when:
        def response = getActiveTopic(TOPIC_ID)

        then:
        extractJson(response, HttpStatus.INTERNAL_SERVER_ERROR) == [code: 500, message: "The topic with id '1' is archived"]
    }

    def "#checkTopicExists returns success when topic exists"() {
        when:
        checkTopicExists(TOPIC_ID)

        then:
        1 * service.checkTopicExists(TOPIC_ID)
    }

    def "#checkTopicExists throws when topic does not exist"() {
        given:
        service.checkTopicExists(NOT_EXISTING_TOPIC_ID) >> { throw TopicNotFoundException.forId(NOT_EXISTING_TOPIC_ID) }

        when:
        def response = checkTopicExists(NOT_EXISTING_TOPIC_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The topic with id '99' could not be found"]
    }

    def "#listForUser returns topics for user default sorted by title ASC"() {
        when:
        def response = extractJson(listForUser(USER_ID))

        then:
        1 * service.listForUser(USER_ID, _) >> topics
        with(response) {
            it.size == 4
            id == [0, 0]
            content == ["Why you little...!", "Eat My Shorts!"]
            archived.every { !it }
            date.every { it instanceof Long }
        }
    }

    private MockHttpServletResponse getById(int id) {
        mockMvc.perform(get(API_BASE_URL + "/topic/" + id).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse getActiveTopic(int id) {
        mockMvc.perform(get(API_BASE_URL + "/topic/active/" + id).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse startTopic(Map json, int id) {
        mockMvc.perform(post(API_BASE_URL + "/topic/user/" + id).contentType(APPLICATION_JSON_VALUE).content(JsonOutput.toJson(json)))
                .andReturn().response
    }

    private MockHttpServletResponse archive(int topicId, int userId) {
        mockMvc.perform(get(API_BASE_URL + "/topic/archive/" + topicId + "/user/" + userId).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse checkTopicExists(int id) {
        mockMvc.perform(get(API_BASE_URL + "/topic/" + id + "/check").contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse listForUser(int userId) {
        mockMvc.perform(get(API_BASE_URL + "/topic/user/" + userId).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }
}
