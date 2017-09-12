package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.entities.Comment
import com.github.rodionovsasha.commentservice.entities.Topic
import com.github.rodionovsasha.commentservice.entities.User
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.repositories.UserRepository
import com.github.rodionovsasha.commentservice.services.impl.UserServiceImpl
import spock.lang.Specification

class UserServiceTest extends Specification {
    def repository = Mock(UserRepository)
    def service = new UserServiceImpl(repository)
    def user = new User()
    def topic = new Topic()
    def comment = new Comment()

    def setup() {
        user.id = 1
        user.name = "Homer"
        user.age = 39
        user.enabled
        user.topics = [topic]
        user.comments = [comment]
    }

    def "should add a new user"() {
        when:
        def result = service.addUser(user)

        then:
        1 * repository.saveAndFlush(user) >> user
        result.name == "Homer"
        result.age == 39
        result.enabled
        result.topics == [topic]
        result.comments == [comment]
    }

    def "should update current user"() {
        given:
        repository.findOne(1) >> user

        when:
        def result = service.updateUser(user)

        then:
        1 * repository.saveAndFlush(user) >> user
        result.name == "Homer"
        result.age == 39
        result.enabled
        result.topics == [topic]
        result.comments == [comment]
    }

    def "should not update user"() {
        given:
        repository.findOne(1)  >> null

        when:
        service.updateUser(user)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "User with id '1' not found"
    }

    def "should delete user"() {
        when:
        service.deleteUser(1)

        then:
        1 * repository.delete(1)
    }

    def "should get user"() {
        when:
        def result = service.getUserById(1)

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
        service.getUserById(1)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "User with id '1' not found"
    }

    def "should get all users"() {
        when:
        def result = service.findAllUsers()

        then:
        1 * repository.findAll() >> [user]
        result[0].name == "Homer"
        result[0].age == 39
        result[0].enabled
        result[0].topics == [topic]
        result[0].comments == [comment]
    }
}
