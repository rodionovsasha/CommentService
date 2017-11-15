package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Comment
import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.ArchivedTopicException
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.services.CommentService
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

class CommentControllerTest extends Specification {
    final TOPIC_ID = 1
    final USER_ID = 1

    def service = Mock(CommentService)
    def controller = new CommentController(service)
    def user = Mock(User)
    def topic = Mock(Topic)
    final DEFAULT_CONTENT = "Why you little...!"
    def defaultComment = new Comment(DEFAULT_CONTENT, user, topic)
    def comments = [defaultComment, new Comment("Eat My Shorts!", user, topic)]

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "#findByTopic returns comments for topic"() {
        when:
        def response = extractJson(findByTopic(TOPIC_ID))

        then:
        1 * service.findByTopic(TOPIC_ID) >> comments
        with(response) {
            it.size == 2
            id == [0, 0]
            content == ["Why you little...!", "Eat My Shorts!"]
            archived.every { !it }
            date.every { it instanceof Long }
        }
    }

    def "#findByTopic does not return comments when topic not exists"() {
        given:
        service.findByTopic(TOPIC_ID) >> { throw TopicNotFoundException.forId(TOPIC_ID) }

        when:
        def response = findByTopic(TOPIC_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The topic with id '1' could not be found"]
    }

    def "#findByTopic returns an empty list when topic is empty"() {
        given:
        service.findByTopic(TOPIC_ID) >> []

        when:
        def response = findByTopic(TOPIC_ID)

        then:
        1 * service.findByTopic(TOPIC_ID)
        response.status == HttpStatus.OK.value()
        response.contentLength == 0
    }

    def "#add creates a new comment"() {
        when:
        def response = extractJson(addComment([content: DEFAULT_CONTENT], TOPIC_ID, USER_ID), HttpStatus.CREATED)

        then:
        1 * service.add(DEFAULT_CONTENT, TOPIC_ID, USER_ID) >> defaultComment
        with(response) {
            id == 0
            content == "Why you little...!"
            !archived
            date instanceof Long
        }
    }

    def "#add does not create a new comment when user does not exist"() {
        given:
        service.add(DEFAULT_CONTENT, TOPIC_ID, USER_ID) >> { throw UserNotFoundException.forId(USER_ID) }

        when:
        def response = addComment([content: DEFAULT_CONTENT], TOPIC_ID, USER_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The user with id '1' could not be found"]
    }

    def "#add does not create a new comment when user is not active"() {
        given:
        service.add(DEFAULT_CONTENT, TOPIC_ID, USER_ID) >> { throw InactiveUserException.forId(USER_ID) }

        when:
        def response = addComment([content: DEFAULT_CONTENT], TOPIC_ID, USER_ID)

        then:
        extractJson(response, HttpStatus.FORBIDDEN) == [code: 403, message: "The user with id '1' is not active"]
    }

    def "#add does not create a new comment when topic is archived"() {
        given:
        service.add(DEFAULT_CONTENT, TOPIC_ID, USER_ID) >> { throw ArchivedTopicException.forId(TOPIC_ID) }

        when:
        def response = addComment([content: DEFAULT_CONTENT], TOPIC_ID, USER_ID)

        then:
        extractJson(response, HttpStatus.INTERNAL_SERVER_ERROR) == [code: 500, message: "The topic with id '1' is archived"]
    }

    def "#add does not create a new comment when content is empty"() {
        when:
        def response = extractJson(addComment([content: ""], TOPIC_ID, USER_ID), HttpStatus.BAD_REQUEST)

        then:
        0 * service.add("", TOPIC_ID, USER_ID)
        with(response) {
            code == 400
            message.contains("may not be empty")
        }
    }

    private MockHttpServletResponse findByTopic(int id) {
        mockMvc.perform(get(API_BASE_URL + "/comment/topic/" + id).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse addComment(Map json, int topicId, int userId) {
        mockMvc.perform(post(API_BASE_URL + "/comment/topic/" + topicId + "/user/" + userId)
                .contentType(APPLICATION_JSON_VALUE).content(JsonOutput.toJson(json)))
                .andReturn().response
    }
}
