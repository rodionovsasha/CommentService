package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Comment
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException
import com.github.rodionovsasha.commentservice.services.CommentService
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class CommentControllerTest extends Specification {
    def service = Mock(CommentService)
    def controller = new CommentController(service)
    def comment = new Comment()
    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def setup() {
        comment.content = "Content"
    }

    def "should get comment"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/comment/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.getCommentById(1) >> comment

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString.contains("\"id\":0,\"content\":\"Content\"")
    }

    def "should not get comment"() {
        given:
        service.getCommentById(1) >> {comment -> throw new CommentNotFoundException("Not found")}

        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/comment/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should add a new comment"() {
        when:
        def newComment = '{"id":1,"content":"Content"}'
        def response = mockMvc.perform(post(API_BASE_URL + "/comment").contentType(APPLICATION_JSON_VALUE).content(newComment)).andReturn().response

        then:
        1 * service.addComment(_) >> comment

        response.status == CREATED.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
    }

    def "should update current comment"() {
        when:
        def updateComment = '{"id":1,"content":"Content"}'
        def response = mockMvc.perform(put(API_BASE_URL + "/comment").contentType(APPLICATION_JSON_VALUE).content(updateComment)).andReturn().response

        then:
        1 * service.updateComment(_) >> comment

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
    }

    def "should not update comment"() {
        given:
        service.updateComment(_) >> {comment -> throw new CommentNotFoundException("Not found")}

        when:
        def updateComment = '{"id":1,"content":"Content"}'
        def response = mockMvc.perform(put(API_BASE_URL + "/comment").contentType(APPLICATION_JSON_VALUE).content(updateComment)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should delete comment"() {
        when:
        def response = mockMvc.perform(delete(API_BASE_URL + "/comment/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.deleteComment(1)

        response.status == NO_CONTENT.value()
    }
}
