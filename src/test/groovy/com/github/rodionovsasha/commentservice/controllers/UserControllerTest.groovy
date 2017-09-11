package com.github.rodionovsasha.commentservice.controllers

import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.services.UserService
import spock.lang.Specification

import static com.github.rodionovsasha.commentservice.AppConfig.API_BASE_URL
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerTest extends Specification {
    def service = Mock(UserService)
    def controller = new UserController(service)
    def user = Mock(User)
    def mockMvc = standaloneSetup(controller).build()

    def setup() {
        user.id >> 1
        user.name >> "Homer"
        user.age >> 39
        user.enabled >> true
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

    def "should delete user"() {
        when:
        def response = mockMvc.perform(delete(API_BASE_URL + "/user/1").contentType(APPLICATION_JSON_VALUE)).andReturn().response

        then:
        1 * service.deleteUser(1)

        response.status == NO_CONTENT.value()
    }
}
