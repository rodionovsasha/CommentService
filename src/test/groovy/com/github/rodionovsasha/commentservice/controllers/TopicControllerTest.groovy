package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.services.TopicService
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static com.github.rodionovsasha.commentservice.controllers.TestUtils.getJsonFromString
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE

class TopicControllerTest extends Specification {
    final TOPIC_ID = 1

    def service = Mock(TopicService)
    def controller = new TopicController(service)
    def user = Mock(User)
    def topic = new Topic("Stupid Flanders", user)

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "should get topic by id"() {
        when:
        def response = getById(TOPIC_ID)

        then:
        1 * service.getById(TOPIC_ID) >> topic
        with(response) {
            status == HttpStatus.OK.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            def responseJson = getJsonFromString(contentAsString)
            responseJson.id == 0
            responseJson.title == "Stupid Flanders"
            !responseJson.archived
            responseJson.date instanceof Long
        }
    }

    def "should not get topic when not exists"() {
        given:
        service.getById(TOPIC_ID) >> {throw TopicNotFoundException.forId(TOPIC_ID)}

        when:
        def response = getById(TOPIC_ID)

        then:
        with(response) {
            status == HttpStatus.NOT_FOUND.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:404, message: "The topic with id '1' could not be found"]
        }
    }

    private MockHttpServletResponse getById(long id) {
        mockMvc.perform(get(API_BASE_URL + "/topic/" + id).contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }
}
