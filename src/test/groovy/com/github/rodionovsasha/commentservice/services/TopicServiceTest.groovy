package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.TopicAccessException
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort

class TopicServiceTest extends BaseTest {
    @Autowired
    TopicService topicService
    @Autowired
    TopicRepository topicRepository
    final HOMER_ID = 1
    final TOPIC_ID = 1
    final NOT_EXISTING_USER_ID = 999

    def "start creates a new topic"() {
        when:
        def topic = topicService.start("D'oh!", HOMER_ID)

        then:
        with(topic) {
            id instanceof Long
            title == "D'oh!"
            !archived
            date instanceof Date
        }
    }

    def "start creates a new topic with owner"() {
        when:
        def topic = topicService.start("D'oh!", HOMER_ID)

        then:
        topic.owner.id == HOMER_ID
    }

    def "start throws when user is inactive"() {
        when:
        topicService.start("D'oh!", 2)

        then:
        thrown(InactiveUserException)
    }

    def "start throws when user not found"() {
        when:
        topicService.start("D'oh!", NOT_EXISTING_USER_ID)

        then:
        thrown(UserNotFoundException)
    }

    def "archive makes a topic archived"() {
        given:
        !topicService.getById(TOPIC_ID).archived

        when:
        topicService.archive(TOPIC_ID, HOMER_ID)

        then:
        topicService.getById(TOPIC_ID).archived
    }

    def "archive throws when user is inactive"() {
        given:
        !topicService.getById(TOPIC_ID).archived

        when:
        topicService.archive(TOPIC_ID, 2)

        then:
        !topicService.getById(TOPIC_ID).archived

        and:
        thrown(InactiveUserException)
    }

    def "archive throws when user not found"() {
        given:
        !topicService.getById(TOPIC_ID).archived

        when:
        topicService.archive(TOPIC_ID, NOT_EXISTING_USER_ID)

        then:
        !topicService.getById(TOPIC_ID).archived

        and:
        thrown(UserNotFoundException)
    }

    def "archive throws when user is not topic owner"() {
        given:
        def TOPIC_ID = 3
        !topicService.getById(TOPIC_ID).archived

        when:
        topicService.archive(TOPIC_ID, HOMER_ID)

        then:
        !topicService.getById(TOPIC_ID).archived

        and:
        def e = thrown(TopicAccessException)
        e.message == "Non-topic owner with id '1' is trying to archive the topic"
    }

    def "archive does not change already archived topic"() {
        given:
        def TOPIC_ID = 7
        topicService.getById(TOPIC_ID).archived

        when:
        topicService.archive(TOPIC_ID, 1)

        then:
        topicService.getById(TOPIC_ID).archived
    }

    def "listForUser returns all topics for user ASC sorted"() {
        when:
        def topics = topicService.listForUser(HOMER_ID, new Sort(Sort.Direction.ASC, "id"))

        then:
        topics.id == [1, 2, 5, 6, 7]
    }

    def "listForUser returns all topics for user DESC sorted"() {
        when:
        def topics = topicService.listForUser(HOMER_ID, new Sort(Sort.Direction.DESC, "id"))

        then:
        topics.id == [7, 6, 5, 2, 1]
    }

    def "listForUser returns an empty list when user does not have own topics"() {
        when:
        def topics = topicService.listForUser(3, new Sort(Sort.Direction.ASC, "id"))

        then:
        topics.isEmpty()
    }

    def "listForUser throws when user is inactive"() {
        when:
        topicService.listForUser(2, null)

        then:
        thrown(InactiveUserException)
    }

    def "listForUser throws when user not found"() {
        when:
        topicService.listForUser(NOT_EXISTING_USER_ID, null)

        then:
        thrown(UserNotFoundException)
    }

    def "search returns topics with a fragment in title"() {
        when:
        def topics = topicService.search("Flanders", 10)

        then:
        topics.id == [6, 1]
    }

    def "search returns topics with a fragment in title case insensitive"() {
        when:
        def topics = topicService.search("FLANDERS", 10)

        then:
        topics.id == [6, 1]
    }

    def "search returns topics with a fragment in title with sorting"() {
        when:
        def topics = topicService.search("Flanders", 10)

        then:
        topics.get(0).date > topics.get(1).date
    }

    def "search returns nothing"() {
        when:
        def topics = topicService.search("Springfield", 10)

        then:
        topics.isEmpty()
    }

    def "search returns limited result"() {
        when:
        def topics = topicService.search("e", 2)

        then:
        topics.size() == 2
    }

    def "getById returns topic"() {
        when:
        def topic = topicService.getById(TOPIC_ID)

        then:
        with(topic) {
            date instanceof Date
            title == "Stupid Flanders"
            !archived
            topic.owner.id == HOMER_ID
        }
    }

    def "getById returns archived topic"() {
        when:
        def topic = topicService.getById(7)

        then:
        topic.archived
    }

    def "getById throws when topic not found"() {
        when:
        topicService.getById(NOT_EXISTING_USER_ID)

        then:
        thrown(TopicNotFoundException)
    }
}
