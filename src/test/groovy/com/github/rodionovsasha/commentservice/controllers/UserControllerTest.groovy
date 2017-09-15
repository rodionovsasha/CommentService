package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.services.impl.UserServiceImpl
import spock.lang.Specification

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerTest extends Specification {
    def service = Mock(UserServiceImpl)
    def controller = new UserController(service)
    def user = new User()
    def mockMvc = standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerController()).build()

/*    def setup() {
        user.id = 1
        user.name = "Homer"
        user.age = 39
        user.enabled
    }

    def "should get user"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.getById(1) >> user

        response.status == OK.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"id":1,"name":"Homer","age":39,"enabled":true}'
    }

    def "should not get user if not exists"() {
        given:
        service.getById(1) >> { user -> throw new UserNotFoundException("Not found")}

        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == NOT_FOUND.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":404,"statusMessage":"Not found"}'
    }

    def "should not get user if id is not correct"() {
        when:
        def response = mockMvc.perform(get(API_BASE_URL + "/user/null").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        response.status == INTERNAL_SERVER_ERROR.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString.contains('{"responseCode":500,"statusMessage":"Failed to convert value of type \'java.lang.String\' to required type \'long\';')
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

    def "should not add a new user when name is empty"() {
        given:
        user.name = ""

        when:
        def newUser = '{"id":1,"name":"","age":39,"enabled":true}'
        def response = mockMvc.perform(post(API_BASE_URL + "/user").contentType(APPLICATION_JSON_VALUE).content(newUser)).andReturn().response

        then:
        0 * service.addUser(_) >> user

        response.status == BAD_REQUEST.value()
        response.contentType == APPLICATION_JSON_UTF8_VALUE
        response.contentAsString == '{"responseCode":400,"statusMessage":"name: may not be empty."}'
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
    }*/
}
