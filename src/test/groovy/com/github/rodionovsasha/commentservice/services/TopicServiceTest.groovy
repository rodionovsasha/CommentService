package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.TopicAccessException
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

    def "archive makes a topic archived"() {
        given:
        def topicId = 1
        !topicService.getById(topicId).archived

        when:
        topicService.archive(topicId, 1)

        then:
        topicService.getById(topicId).archived
    }

    def "archive throws when user is inactive"() {
        given:
        def topicId = 1
        !topicService.getById(topicId).archived

        when:
        topicService.archive(topicId, 2)

        then:
        !topicService.getById(topicId).archived
        def e = thrown(InactiveUserException)
        e.message == "The user with id '2' is not active"
    }

    def "archive throws when user not found"() {
        given:
        def topicId = 1
        !topicService.getById(topicId).archived

        when:
        topicService.archive(1, 999)

        then:
        !topicService.getById(topicId).archived
        def e = thrown(UserNotFoundException)
        e.message == "The user with id '999' could not be found"
    }

    def "archive throws when user is not topic owner"() {
        given:
        def topicId = 2
        !topicService.getById(topicId).archived

        when:
        topicService.archive(2, 1)

        then:
        !topicService.getById(topicId).archived
        def e = thrown(TopicAccessException)
        e.message == "Non-topic owner with id '1' is trying to archive the topic"
    }
}
