package com.github.rodionovsasha.commentservice.services

import spock.lang.Specification

class TopicServiceTest extends Specification {
/*    def repository = Mock(TopicRepository)
    def service = new TopicServiceImpl(repository)
    def topic = new Topic()
    def comment = new Comment()

    def setup() {
        topic.name = "Topic name"
        topic.comments = [comment]
    }

    def "should add a new topic"() {
        when:
        def result = service.addTopic(topic)

        then:
        1 * repository.saveAndFlush(topic) >> topic
        result.name == "Topic name"
        result.comments == [comment]
    }

    def "should update current topic"() {
        given:
        repository.findOne(0) >> topic

        when:
        def result = service.updateTopic(topic)

        then:
        1 * repository.saveAndFlush(topic) >> topic
        result.name == "Topic name"
        result.comments == [comment]
    }

    def "should not update topic"() {
        given:
        repository.findOne(0)  >> null

        when:
        service.updateTopic(topic)

        then:
        def e = thrown(TopicNotFoundException)
        e.message == "Topic with id '0' not found"
    }

    def "should delete topic"() {
        when:
        service.deleteTopic(0)

        then:
        1 * repository.delete(0)
    }

    def "should get topic"() {
        when:
        def result = service.getTopicById(0)

        then:
        1 * repository.findOne(0) >> topic
        result.name == "Topic name"
        result.comments == [comment]
    }

    def "should not get topic"() {
        given:
        repository.findOne(0)  >> null

        when:
        service.getTopicById(0)

        then:
        def e = thrown(TopicNotFoundException)
        e.message == "Topic with id '0' not found"
    }*/
}
