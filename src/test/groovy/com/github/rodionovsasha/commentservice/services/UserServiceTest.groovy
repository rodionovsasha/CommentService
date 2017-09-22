package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException

class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService

    def "should get user by id"() {
        when:
        def user = userService.getById(1)

        then:
        user.id == 1
        user.name == "Homer"
        user.age == 39
        user.enabled
    }

    def "should not get user by id"() {
        when:
        userService.getById(999)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }

    def "should get active user by id"() {
        when:
        def user = userService.getActiveUser(1)

        then:
        user.id == 1
        user.name == "Homer"
        user.age == 39
        user.enabled
    }

    def "should not get active user by id"() {
        when:
        userService.getActiveUser(2)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "should update user name"() {
        when:
        userService.updateName(1, "Maggie")

        then:
        def user =  userService.getById(1)

        and:
        user.id == 1
        user.name == "Maggie"
        user.age == 39
        user.enabled
    }

    def "should not update user name"() {
        when:
        userService.updateName(2, "Maggie")

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "should update user age"() {
        when:
        userService.updateAge(1, 35)

        then:
        def user =  userService.getById(1)

        and:
        user.id == 1
        user.name == "Homer"
        user.age == 35
        user.enabled
    }

    def "should not update user age"() {
        when:
        userService.updateAge(2, 35)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "should deactivate user"() {
        when:
        userService.deactivate(1)

        then:
        def user =  userService.getById(1)

        and:
        user.id == 1
        user.name == "Homer"
        user.age == 39
        !user.enabled
    }

    def "should not deactivate user"() {
        when:
        userService.deactivate(2)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "should activate user"() {
        when:
        userService.activate(2)

        then:
        def user =  userService.getById(2)

        and:
        user.id == 2
        user.name == "Bart"
        user.age == 10
        user.enabled
    }

    def "should not activate user"() {
        when:
        userService.activate(999)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }

    def "should delete user"() {
        when:
        userService.delete(1)
        userService.getById(1)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '1' could not be found"
    }

    def "should not delete user"() {
        when:
        userService.delete(999)

        then:
        thrown(EmptyResultDataAccessException)
    }

    def "should create new user"() {
        when:
        def user = userService.create("Marge", 37)

        then:
        user.id == 3
        user.name == "Marge"
        user.age == 37
        user.enabled
    }
}
