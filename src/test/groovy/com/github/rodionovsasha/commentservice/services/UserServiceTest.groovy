package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.entities.Comment
import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.repositories.UserRepository
import com.github.rodionovsasha.commentservice.services.impl.UserServiceImpl
import spock.lang.Specification

class UserServiceTest extends Specification {
    def repository = Mock(UserRepository)
    def service = new UserServiceImpl(repository)
    def user
    def name = "Homer"
    def age = 39
    def topic = new Topic()
    def comment = new Comment()

    def setup() {
        user = new User(name, age)
        user.enabled
        user.topics = [topic]
        user.comments = [comment]
    }

    def "should create a new user"() {
        when:
        def result = service.create(name, age)

        then:
        1 * repository.saveAndFlush(_) >> user
        result.name == "Homer"
        result.age == 39
        result.enabled
        result.topics == [topic]
        result.comments == [comment]
    }

    def "should get user"() {
        when:
        def result = service.getById(1)

        then:
        1 * repository.findOne(1) >> user
        result.name == "Homer"
        result.age == 39
        result.enabled
        result.topics == [topic]
        result.comments == [comment]
    }

    def "should not get user"() {
        given:
        repository.findOne(1)  >> null

        when:
        service.getById(1)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "User with id '1' not found"
    }

    def "should get active user"() {
        when:
        def result = service.getActive(1)

        then:
        1 * repository.findOne(1) >> user
        result.name == "Homer"
        result.age == 39
        result.enabled
        result.topics == [topic]
        result.comments == [comment]
    }

    def "should get not active user"() {
        given:
        user.enabled = false

        when:
        service.getActive(1)

        then:
        1 * repository.findOne(1) >> user
        def e = thrown(InactiveUserException)
        e.message == "User with userId '1' is not active"
    }

    def "should update user name"() {
        given:
        user.name = "Bart"
        repository.findOne(1) >> user

        when:
        service.updateName(1, "Bart")

        then:
        1 * repository.saveAndFlush(user)
    }

    def "should not update name if user does not exist"() {
        given:
        repository.findOne(1)  >> null

        when:
        service.updateName(1, "Bart")

        then:
        def e = thrown(UserNotFoundException)
        e.message == "User with id '1' not found"
    }

    def "should update user age"() {
        given:
        user.age = 10
        repository.findOne(1) >> user

        when:
        service.updateAge(1, 10)

        then:
        1 * repository.saveAndFlush(user)
    }

    def "should not update age if user does not exist"() {
        given:
        repository.findOne(1)  >> null

        when:
        service.updateAge(1, 10)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "User with id '1' not found"
    }

    def "should deactivate user"() {
        given:
        user.enabled = false
        repository.findOne(1) >> user

        when:
        service.deactivate(1)

        then:
        1 * repository.saveAndFlush(user)
    }

    def "should not deactivate user if user does not exist"() {
        given:
        repository.findOne(1)  >> null

        when:
        service.deactivate(1)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "User with id '1' not found"
    }

    def "should delete user"() {
        when:
        service.delete(1)

        then:
        1 * repository.delete(1)
    }
}
