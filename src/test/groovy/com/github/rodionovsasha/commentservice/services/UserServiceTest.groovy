package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService
    @Autowired
    UserRepository userRepository

    def "getActiveUser returns active user by id"() {
        when:
        def user = userService.getActiveUser(1)

        then:
        user.name == "Homer"
        user.age == 39
        user.active
    }

    def "getActiveUser throws when user is inactive"() {
        when:
        userService.getActiveUser(2)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "getActiveUser throws when user not found"() {
        when:
        userService.getActiveUser(999)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }

    def "updateName does update user's name"() {
        given:
        userRepository.getOne(1L).name == "Homer"

        when:
        userService.updateName(1, "Maggie")

        then:
        def user = userRepository.getOne(1L)
        user.name == "Maggie"
    }

    def "updateName throws when user is not active"() {
        when:
        userService.updateName(2, "Maggie")

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "updateAge does update user's age"() {
        given:
        userRepository.getOne(1L).age == 39

        when:
        userService.updateAge(1, 35)

        then:
        def user =  userRepository.getOne(1L)
        user.age == 35
    }

    def "updateAge throws when user is not active"() {
        when:
        userService.updateAge(2, 35)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "deactivate makes user inactive"() {
        given:
        userRepository.getOne(1L).active

        when:
        userService.deactivate(1)

        then:
        def user =  userRepository.getOne(1L)
        !user.active
    }

    def "deactivate throws when user is not active"() {
        when:
        userService.deactivate(2)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "activate does user active"() {
        given:
        !userRepository.getOne(2L).active

        when:
        userService.activate(2)

        then:
        def user = userRepository.getOne(2L)
        user.active
    }

    def "activate throws when user is not found"() {
        when:
        userService.activate(999)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }

    def "create creates a new user"() {
        when:
        def user = userService.create("Marge", 37)

        then:
        user.name == "Marge"
        user.age == 37
        user.active
    }
}
