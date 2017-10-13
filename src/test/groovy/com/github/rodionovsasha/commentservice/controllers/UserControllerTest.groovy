package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.services.UserService
import org.springframework.http.HttpStatus
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static com.github.rodionovsasha.commentservice.controllers.TestUtils.getJsonFromString
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE

class UserControllerTest extends Specification {
    final HOMER_ID = 1
    final GET_HOMER_URL = API_BASE_URL + "/user/1"

    def service = Mock(UserService)
    def controller = new UserController(service)
    def user = new User(id: HOMER_ID, name: "Homer", age: 39, active: true)

    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def "should get active user"() {
        when:
        def response = mockMvc.perform(get(GET_HOMER_URL).contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.getActiveUser(HOMER_ID) >> user
        with(response) {
            status == HttpStatus.OK.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [id: 1, name: "Homer", age: 39, active: true]
        }
    }



    def "should not get active user if not exists"() {
        given:
        service.getActiveUser(HOMER_ID) >> { user -> throw UserNotFoundException.forId(HOMER_ID) }

        when:
        def response = mockMvc.perform(get(GET_HOMER_URL).contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        with(response) {
            status == HttpStatus.NOT_FOUND.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:404, message: "The user with id '1' could not be found"]
        }
    }

    def "should not get active user if deactivated"() {
        given:
        service.getActiveUser(HOMER_ID) >> { user -> throw InactiveUserException.forId(HOMER_ID) }

        when:
        def response = mockMvc.perform(get(GET_HOMER_URL).contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        with(response) {
            status == HttpStatus.FORBIDDEN.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:403, message: "The user with id '1' is not active"]
        }
    }

    def "should not get user if id has wrong type"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/null").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        with(response) {
            status == HttpStatus.INTERNAL_SERVER_ERROR.value()
            contentType == APPLICATION_JSON_UTF8_VALUE
            getJsonFromString(contentAsString) == [code:500, message: "Failed to convert value of type 'java.lang.String' to required type 'long'; nested exception is java.lang.NumberFormatException: For input string: \"null\""]
        }
    }
}
