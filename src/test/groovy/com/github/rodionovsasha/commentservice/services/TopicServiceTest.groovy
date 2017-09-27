package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired

class TopicServiceTest extends BaseTest {
    @Autowired
    TopicService topicService
    @Autowired
    TopicRepository topicRepository

    def "start creates a new topic"() {
        when:
        def topic = topicService.start("Doh", 1)

        then:
        topic.id instanceof Long
        topic.title == "Doh"
        !topic.archived
        topic.date instanceof Date
    }

    def "start creates a new topic with owner"() {
        when:
        def topic = topicService.start("Doh", 1)

        then:
        topic.owner.id instanceof Long
        topic.owner.active
        topic.owner.age == 39
        topic.owner.name == "Homer"
    }

    def "start throws when user is inactive"() {
        when:
        topicService.start("Doh", 2)

        then:
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "start throws when user not found"() {
        when:
        topicService.start("Doh", 999)

        then:
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }
}
