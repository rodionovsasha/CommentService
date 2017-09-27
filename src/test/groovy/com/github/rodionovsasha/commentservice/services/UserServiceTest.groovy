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

    def "getActiveUser returns topics for active user by id"() {
        when:
        def user = userService.getActiveUser(1)

        then:
        user.topics.size() == 2
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

    def "checkUserActive checks user by id"() {
        expect:
        userService.checkUserActive(1)
    }

    def "checkUserActive throws when user is inactive"() {
        when:
        userService.checkUserActive(2)

        then:
        thrown(InactiveUserException)
    }

    def "checkUserActive throws when user not found"() {
        when:
        userService.checkUserActive(999)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }

    def "updateName does update user's name"() {
        given:
        def id = 1L
        userRepository.getOne(id).name == "Homer"

        when:
        userService.updateName(id, "Maggie")

        then:
        userRepository.getOne(id).name == "Maggie"
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
        def id = 1L
        userRepository.getOne(id).age == 39

        when:
        userService.updateAge(id, 35)

        then:
        userRepository.getOne(id).age == 35
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
        def id = 1L
        userRepository.getOne(id).active

        when:
        userService.deactivate(id)

        then:
        !userRepository.getOne(id).active
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
        def id = 2L
        !userRepository.getOne(id).active

        when:
        userService.activate(id)

        then:
        userRepository.getOne(id).active
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
        user.id instanceof Long
        user.name == "Marge"
        user.age == 37
        user.active
    }

    def "creating a new user does not create a new topic"() {
        when:
        def user = userService.create("Marge", 37)

        then:
        user.topics.size() == 0
    }
}
