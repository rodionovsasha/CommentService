package com.github.rodionovsasha.commentservice.services

import spock.lang.Specification

class CommentServiceTest extends Specification {
/*    def repository = Mock(CommentRepository)
    def service = new CommentServiceImpl(repository)
    def comment = new Comment()

    def setup() {
        comment.content = "Content"
    }

    def "should add a new comment"() {
        when:
        def result = service.add(comment)

        then:
        1 * repository.saveAndFlush(comment) >> comment
        result.content == "Content"
    }

    def "should update current comment"() {
        given:
        repository.findOne(0) >> comment

        when:
        def result = service.update(comment)

        then:
        1 * repository.saveAndFlush(comment) >> comment
        result.content == "Content"
    }

    def "should not update comment"() {
        given:
        repository.findOne(0)  >> null

        when:
        service.update(comment)

        then:
        def e = thrown(CommentNotFoundException)
        e.message == "Comment with id '0' not found"
    }

    def "should delete comment"() {
        when:
        service.delete(0)

        then:
        1 * repository.delete(0)
    }

    def "should get comment"() {
        when:
        def result = service.getById(0)

        then:
        1 * repository.findOne(0) >> comment
        result.content == "Content"
    }

    def "should not get comment"() {
        given:
        repository.findOne(0)  >> null

        when:
        service.getById(0)

        then:
        def e = thrown(CommentNotFoundException)
        e.message == "Comment with id '0' not found"
    }*/
}
