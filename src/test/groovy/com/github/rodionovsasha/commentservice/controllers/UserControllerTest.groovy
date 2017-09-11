package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.services.UserService
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.Application.API_BASE_URL
import static org.springframework.http.HttpStatus.*
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerTest extends Specification {
    def service = Mock(UserService)
    def controller = new UserController(service)
    def user = new User()
    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

    def setup() {
        user.id = 1
        user.name = "Homer"
        user.age = 39
        user.enabled
    }


    def "should get all users"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL).contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.findAllUsers() >> [user]

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '[{"id":1,"name":"Homer","age":39,"enabled":true}]'
    }

    def "should get user"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.getUserById(1) >> user

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"id":1,"name":"Homer","age":39,"enabled":true}'
    }

    def "should not get user"() {
        given:
        service.getUserById(1) >> {user -> throw new UserNotFoundException("Not found")}

        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should add a new user"() {
        when:
        def newUser = '{"id":1,"name":"Homer","age":39,"enabled":true}'
        def response = mockMvc.perform(post(API_BASE_URL + "/user").contentType(APPLICATION_JSON_VALUE).content(newUser)).andReturn().response

        then:
        1 * service.addUser(_) >> user

        response.status == CREATED.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
    }

    def "should update current user"() {
        when:
        def updateUser = '{"id":1,"name":"Homer","age":39,"enabled":true}'
        def response = mockMvc.perform(put(API_BASE_URL + "/user").contentType(APPLICATION_JSON_VALUE).content(updateUser)).andReturn().response

        then:
        1 * service.updateUser(_) >> user

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
    }

    def "should not update user"() {
        given:
        service.updateUser(_) >> {user -> throw new UserNotFoundException("Not found")}

        when:
        def updateUser = '{"id":1,"name":"Homer","age":39,"enabled":true}'
        def response = mockMvc.perform(put(API_BASE_URL + "/user").contentType(APPLICATION_JSON_VALUE).content(updateUser)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should delete user"() {
        when:
        def response = mockMvc.perform(delete(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.deleteUser(1)

        response.status == NO_CONTENT.value()
    }
}
