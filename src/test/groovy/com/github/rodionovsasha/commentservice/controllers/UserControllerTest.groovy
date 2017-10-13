package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.services.UserService
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

class UserControllerTest extends Specification {
    final HOMER_ID = 1

    def service = Mock(UserService)
    def controller = new UserController(service)
    def user = new User(id: HOMER_ID, name: "Homer", age: 39, active: true)

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "should get active user"() {
        when:
        def response = getUserHomer()

        then:
        1 * service.getActiveUser(HOMER_ID) >> user
        with(response) {
            status == HttpStatus.OK.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [id: 1, name: "Homer", age: 39, active: true]
        }
    }

    def "should not get active user when not exists"() {
        given:
        service.getActiveUser(HOMER_ID) >> { user -> throw UserNotFoundException.forId(HOMER_ID) }

        when:
        def response = getUserHomer()

        then:
        with(response) {
            status == HttpStatus.NOT_FOUND.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:404, message: "The user with id '1' could not be found"]
        }
    }

    def "should not get active user when deactivated"() {
        given:
        service.getActiveUser(HOMER_ID) >> { user -> throw InactiveUserException.forId(HOMER_ID) }

        when:
        def response = getUserHomer()

        then:
        with(response) {
            status == HttpStatus.FORBIDDEN.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:403, message: "The user with id '1' is not active"]
        }
    }

    def "should not get user when id has wrong type"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/null").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        with(response) {
            status == HttpStatus.INTERNAL_SERVER_ERROR.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            def responseJson = getJsonFromString(contentAsString)
            responseJson.code == 500
            responseJson.message.contains("Failed to convert value of type 'java.lang.String' to required type 'long'")
        }
    }

    def "should create user"() {
        given:
        service.create("Homer", 39) >> user

        when:
        def response = createUser([name: "Homer", age: 39])

        then:
        with(response) {
            status == HttpStatus.CREATED.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [id: 1, name: "Homer", age: 39, active: true]
        }
    }

    def "should not create user when name is empty"() {
        given:
        service.create("", 39) >> user

        when:
        def response = createUser([name: "", age: 39])

        then:
        with(response) {
            status == HttpStatus.INTERNAL_SERVER_ERROR.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            def responseJson = getJsonFromString(contentAsString)
            responseJson.code == 500
            responseJson.message.contains("Field error in object 'user' on field 'name': rejected value [];")
        }
    }

    private MockHttpServletResponse getUserHomer() {
        mockMvc.perform(get(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse createUser(Map json) {
        mockMvc.perform(post(API_BASE_URL + "/user").contentType(APPLICATION_JSON_VALUE).content(JsonOutput.toJson(json)))
                .andReturn().response
    }
}
