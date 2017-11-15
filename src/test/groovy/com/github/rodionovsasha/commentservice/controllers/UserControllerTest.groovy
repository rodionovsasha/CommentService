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
import static com.github.rodionovsasha.commentservice.controllers.TestUtils.extractJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE

class UserControllerTest extends Specification {
    final HOMER_ID = 1
    final NOT_EXISTING_USER_ID = 999

    def service = Mock(UserService)
    def controller = new UserController(service)
    def user = new User(id: HOMER_ID, name: "Homer", age: 39, active: true)

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "should get active user"() {
        when:
        def response = getUserHomer()

        then:
        1 * service.getActiveUser(HOMER_ID) >> user
        extractJson(response, HttpStatus.OK) == [id: HOMER_ID, name: "Homer", age: 39, active: true]
    }

    def "should not get active user when not exists"() {
        given:
        service.getActiveUser(HOMER_ID) >> { user -> throw UserNotFoundException.forId(HOMER_ID) }

        when:
        def response = getUserHomer()

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The user with id '1' could not be found"]
    }

    def "should not get active user when deactivated"() {
        given:
        service.getActiveUser(HOMER_ID) >> { user -> throw InactiveUserException.forId(HOMER_ID) }

        when:
        def response = getUserHomer()

        then:
        extractJson(response, HttpStatus.FORBIDDEN) == [code: 403, message: "The user with id '1' is not active"]
    }

    def "should not get user when id has wrong type"() {
        when:
        def response = extractJson(mockMvc.perform(get(API_BASE_URL + "/user/null").contentType(APPLICATION_JSON_VALUE)).andReturn().response,
                HttpStatus.INTERNAL_SERVER_ERROR)

        then:
        with(response) {
            code == 500
            message.contains("Failed to convert value of type 'java.lang.String' to required type 'int'")
        }
    }

    def "should create user"() {
        given:
        service.create("Homer", 39) >> user

        when:
        def response = createUser([name: "Homer", age: 39])

        then:
        extractJson(response, HttpStatus.CREATED) == [id: HOMER_ID, name: "Homer", age: 39, active: true]
    }

    def "should not create user when name is empty"() {
        given:
        service.create("", 39) >> user

        when:
        def response = extractJson(createUser([name: "", age: 39]), HttpStatus.BAD_REQUEST)

        then:
        with(response) {
            code == 400
            message.contains("Field error in object 'user' on field 'name': rejected value [];")
        }
    }

    def "should update user name"() {
        when:
        def response = updateName([id: 1, name: "Homer the Genius"])

        then:
        1 * service.updateName(HOMER_ID, "Homer the Genius")
        response.status == HttpStatus.OK.value()
    }

    def "should not update user with empty name"() {
        when:
        def response = extractJson(updateName([id: HOMER_ID, name: ""]), HttpStatus.BAD_REQUEST)

        then:
        0 * service.updateName(HOMER_ID, "")
        with(response) {
            code == 400
            message.contains("Field error in object 'user' on field 'name': rejected value [];")
        }
    }

    def "should not update user name when user not exist"() {
        given:
        service.updateName(NOT_EXISTING_USER_ID, "Homer the Genius") >> { user -> throw UserNotFoundException.forId(NOT_EXISTING_USER_ID) }

        when:
        def response = updateName([id: NOT_EXISTING_USER_ID, name: "Homer the Genius"])

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The user with id '999' could not be found"]
    }

    def "should update user age"() {
        when:
        def response = updateAge([id: 1, age: 39])

        then:
        1 * service.updateAge(HOMER_ID, 39)
        response.status == HttpStatus.OK.value()
    }

    def "should update user with empty age"() {
        when:
        def response = updateAge([id: HOMER_ID, age: ""])

        then:
        1 * service.updateAge(HOMER_ID, _)
        response.status == HttpStatus.OK.value()
    }

    def "should not update user age when user not exist"() {
        given:
        service.updateAge(NOT_EXISTING_USER_ID, 39) >> { user -> throw UserNotFoundException.forId(NOT_EXISTING_USER_ID) }

        when:
        def response = updateAge([id: NOT_EXISTING_USER_ID, age: 39])

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The user with id '999' could not be found"]
    }

    def "should deactivate user"() {
        when:
        def response = deactivate(HOMER_ID)

        then:
        1 * service.deactivate(HOMER_ID)
        response.status == HttpStatus.OK.value()
    }

    def "should not deactivate user when user not exist"() {
        given:
        service.deactivate(NOT_EXISTING_USER_ID) >> { user -> throw UserNotFoundException.forId(NOT_EXISTING_USER_ID) }

        when:
        def response = deactivate(NOT_EXISTING_USER_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The user with id '999' could not be found"]
    }

    def "should activate user"() {
        when:
        def response = activate(HOMER_ID)

        then:
        1 * service.activate(HOMER_ID)
        response.status == HttpStatus.OK.value()
    }

    def "should not activate user when user not exist"() {
        given:
        service.activate(NOT_EXISTING_USER_ID) >> { user -> throw UserNotFoundException.forId(NOT_EXISTING_USER_ID) }

        when:
        def response = activate(NOT_EXISTING_USER_ID)

        then:
        extractJson(response, HttpStatus.NOT_FOUND) == [code: 404, message: "The user with id '999' could not be found"]
    }

    private MockHttpServletResponse getUserHomer() {
        mockMvc.perform(get(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse createUser(Map json) {
        mockMvc.perform(post(API_BASE_URL + "/user").contentType(APPLICATION_JSON_VALUE).content(JsonOutput.toJson(json)))
                .andReturn().response
    }

    private MockHttpServletResponse updateName(Map json) {
        update("/user/name", json)
    }

    private MockHttpServletResponse updateAge(Map json) {
        update("/user/age", json)
    }

    private MockHttpServletResponse update(String path, Map json) {
        mockMvc.perform(put(API_BASE_URL + path).contentType(APPLICATION_JSON_VALUE).content(JsonOutput.toJson(json)))
                .andReturn().response
    }

    private MockHttpServletResponse deactivate(int id) {
        mockMvc.perform(get(API_BASE_URL + "/user/" + id + "/deactivate").contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }

    private MockHttpServletResponse activate(int id) {
        mockMvc.perform(get(API_BASE_URL + "/user/" + id + "/activate").contentType(APPLICATION_JSON_VALUE))
                .andReturn().response
    }
}
