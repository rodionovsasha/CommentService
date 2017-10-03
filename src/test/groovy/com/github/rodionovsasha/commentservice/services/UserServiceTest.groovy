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
    final HOMER_ID = 1L
    final NOT_EXISTING_USER_ID = 999

    def "getActiveUser returns active user by id"() {
        when:
        def user = userService.getActiveUser(HOMER_ID)

        then:
        with(user) {
            name == "Homer"
            age == 39
            active
        }
    }

    def "getActiveUser returns topics for active user by id"() {
        expect:
        userService.getActiveUser(HOMER_ID).topics.size() == 5
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
        userService.getActiveUser(NOT_EXISTING_USER_ID)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '" + NOT_EXISTING_USER_ID + "' could not be found"
    }

    def "checkUserActive checks user by id"() {
        expect:
        userService.checkUserActive(HOMER_ID)
    }

    def "checkUserActive throws when user is inactive"() {
        when:
        userService.checkUserActive(2)

        then:
        thrown(InactiveUserException)
    }

    def "checkUserActive throws when user not found"() {
        when:
        userService.checkUserActive(NOT_EXISTING_USER_ID)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '" + NOT_EXISTING_USER_ID + "' could not be found"
    }

    def "updateName does update user's name"() {
        given:
        userRepository.getOne(HOMER_ID).name == "Homer"

        when:
        userService.updateName(HOMER_ID, "Maggie")

        then:
        userRepository.getOne(HOMER_ID).name == "Maggie"
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
        userRepository.getOne(HOMER_ID).age == 39

        when:
        userService.updateAge(HOMER_ID, 35)

        then:
        userRepository.getOne(HOMER_ID).age == 35
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
        userRepository.getOne(HOMER_ID).active

        when:
        userService.deactivate(HOMER_ID)

        then:
        !userRepository.getOne(HOMER_ID).active
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
        userService.activate(NOT_EXISTING_USER_ID)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '" + NOT_EXISTING_USER_ID + "' could not be found"
    }

    def "create creates a new user"() {
        when:
        def user = userService.create("Marge", 37)

        then:
        with(user) {
            id instanceof Long
            name == "Marge"
            age == 37
            active
        }
    }
}
