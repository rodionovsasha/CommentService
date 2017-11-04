package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Comment
import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.ArchivedTopicException
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.services.CommentService
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static com.github.rodionovsasha.commentservice.controllers.TestUtils.extractJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE

class CommentControllerTest extends Specification {
    final COMMENT_ID = 1
    final TOPIC_ID = 1
    final USER_ID = 1
    final NOT_EXISTING_COMMENT_ID = 99

    def service = Mock(CommentService)
    def controller = new CommentController(service)
    def user = Mock(User)
    def topic = Mock(Topic)
    def comments = [new Comment("Why you little...!", user, topic),
                    new Comment("Eat My Shorts!", user, topic)]

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
            [!archived, !archived]
            [date instanceof Long, date instanceof Long]
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

    private MockHttpServletResponse findByTopic(int id) {
        mockMvc.perform(get(API_BASE_URL + "/comment/topic/" + id).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }
}
