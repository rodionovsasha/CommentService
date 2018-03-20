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
        userService.getActiveUser(BART_ID)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '" + BART_ID + "' is not active"
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
        userService.checkUserActive(BART_ID)

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
        userRepository.findById(HOMER_ID).get().name == "Homer"

        when:
        userService.updateName(HOMER_ID, "Maggie")

        then:
        userRepository.findById(HOMER_ID).get().name == "Maggie"
    }

    def "updateName throws when user is not active"() {
        when:
        userService.updateName(BART_ID, "Maggie")

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '" + BART_ID + "' is not active"
    }

    def "updateAge does update user's age"() {
        given:
        userRepository.findById(HOMER_ID).get().age == 39

        when:
        userService.updateAge(HOMER_ID, 35)

        then:
        userRepository.findById(HOMER_ID).get().age == 35
    }

    def "updateAge throws when user is not active"() {
        when:
        userService.updateAge(BART_ID, 35)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '" + BART_ID + "' is not active"
    }

    def "deactivate makes user inactive"() {
        given:
        userRepository.findById(HOMER_ID).get().active

        when:
        userService.deactivate(HOMER_ID)

        then:
        !userRepository.findById(HOMER_ID).get().active
    }

    def "deactivate throws when user is not active"() {
        when:
        userService.deactivate(BART_ID)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '" + BART_ID + "' is not active"
    }

    def "activate does user active"() {
        given:
        !userRepository.findById(BART_ID).get().active

        when:
        userService.activate(BART_ID)

        then:
        userRepository.findById(BART_ID).get().active
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
            id instanceof Integer
            name == "Marge"
            age == 37
            active
        }
    }
}
