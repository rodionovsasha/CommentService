package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.*
import com.github.rodionovsasha.commentservice.repositories.CommentRepository
import com.github.vkorobkov.jfixtures.IntId
import org.springframework.beans.factory.annotation.Autowired

class CommentServiceTest extends BaseTest {
    final COMMENT_CONTENT = "I will never return money borrowed from stupid Flanders"

    @Autowired
    CommentService commentService
    @Autowired
    CommentRepository repository

    def "add adds a new comment"() {
        when:
        def comment = commentService.add(COMMENT_CONTENT, TOPIC_ID, HOMER_ID)

        then:
        with(comment) {
            id instanceof Integer
            content == COMMENT_CONTENT
            date instanceof Date
        }
    }

    def "add adds a new comment with owner"() {
        expect:
        commentService.add(COMMENT_CONTENT, TOPIC_ID, HOMER_ID).user.id == HOMER_ID
    }

    def "add adds a new comment with parent topic"() {
        expect:
        commentService.add(COMMENT_CONTENT, TOPIC_ID, HOMER_ID).topic.id == TOPIC_ID
    }

    def "add throws when user is inactive"() {
        when:
        commentService.add(COMMENT_CONTENT, TOPIC_ID, BART_ID)

        then:
        thrown(InactiveUserException)
    }

    def "add throws when user not found"() {
        when:
        commentService.add(COMMENT_CONTENT, TOPIC_ID, NOT_EXISTING_USER_ID)

        then:
        thrown(UserNotFoundException)
    }

    def "add throws when topic is archived"() {
        when:
        commentService.add(COMMENT_CONTENT, ARCHIVED_TOPIC_ID, HOMER_ID)

        then:
        thrown(ArchivedTopicException)
    }

    def "add throws when topic not found"() {
        when:
        commentService.add(COMMENT_CONTENT, NOT_EXISTING_TOPIC_ID, HOMER_ID)

        then:
        thrown(TopicNotFoundException)
    }

    def "update updates a comment"() {
        given:
        def oldContent = repository.getOne(COMMENT_ID).content

        when:
        commentService.update(COMMENT_ID, HOMER_ID, COMMENT_CONTENT)

        then:
        def content = repository.getOne(COMMENT_ID).content
        content == COMMENT_CONTENT

        and:
        content != oldContent
    }

    def "update throws when user is not active"() {
        when:
        commentService.update(IntId.one("comment2"), BART_ID, COMMENT_CONTENT)

        then:
        thrown(InactiveUserException)
    }

    def "update throws when user not found"() {
        when:
        commentService.update(IntId.one("comment2"), NOT_EXISTING_USER_ID, COMMENT_CONTENT)

        then:
        thrown(UserNotFoundException)
    }

    def "update throws when comment not found"() {
        when:
        commentService.update(NOT_EXISTING_COMMENT_ID, HOMER_ID, COMMENT_CONTENT)

        then:
        def e = thrown(CommentNotFoundException)
        e.message == "The comment with id '" + NOT_EXISTING_COMMENT_ID + "' could not be found"
    }

    def "update throws when user updates not own comment"() {
        when:
        commentService.update(IntId.one("comment2"), HOMER_ID, COMMENT_CONTENT)

        then:
        def e = thrown(CommentAccessException)
        e.message == "Non-comment owner with id '" + HOMER_ID + "' is trying to update the comment"
    }

    def "archive makes a comment archived"() {
        given:
        !repository.getOne(COMMENT_ID).archived

        when:
        commentService.archive(COMMENT_ID, HOMER_ID)

        then:
        repository.getOne(COMMENT_ID).archived
    }

    def "archive throws when user is inactive"() {
        given:
        !repository.getOne(COMMENT_ID).archived

        when:
        commentService.archive(COMMENT_ID, BART_ID)

        then:
        !repository.getOne(COMMENT_ID).archived

        and:
        thrown(InactiveUserException)
    }

    def "archive throws when user not found"() {
        given:
        !repository.getOne(COMMENT_ID).archived

        when:
        commentService.archive(COMMENT_ID, NOT_EXISTING_USER_ID)

        then:
        !repository.getOne(COMMENT_ID).archived

        and:
        thrown(UserNotFoundException)
    }

    def "archive throws when comment not found"() {
        when:
        commentService.archive(NOT_EXISTING_COMMENT_ID, HOMER_ID)

        then:
        def e = thrown(CommentNotFoundException)
        e.message == "The comment with id '" + NOT_EXISTING_COMMENT_ID + "' could not be found"
    }

    def "archive throws when user updates not own comment"() {
        given:
        def COMMENT_ID = IntId.one("comment2")
        !repository.getOne(COMMENT_ID).archived

        when:
        commentService.archive(COMMENT_ID, HOMER_ID)

        then:
        !repository.getOne(COMMENT_ID).archived

        and:
        def e = thrown(CommentAccessException)
        e.message == "Non-comment owner with id '" + HOMER_ID + "' is trying to update the comment"
    }

    def "archive does not change already archived comment"() {
        given:
        repository.getOne(ARCHIVED_COMMENT_ID).archived

        when:
        commentService.archive(ARCHIVED_COMMENT_ID, HOMER_ID)

        then:
        repository.getOne(ARCHIVED_COMMENT_ID).archived
    }

    def "findByTopic returns comments for topic"() {
        expect:
        commentService.findByTopic(TOPIC_ID).id ==
                IntId.many("stupid_flanders_comment1", "stupid_flanders_comment2", "stupid_flanders_comment3", "stupid_flanders_comment5")
    }

    def "findByTopic returns comments for topic ASC sorted"() {
        when:
        def comments = commentService.findByTopic(TOPIC_ID)

        then:
        with(comments) {
            get(0).date < get(1).date
            get(1).date < get(2).date
            get(2).date < get(3).date
        }
    }

    def "findByTopic does not return archived comments for topic"() {
        given:
        def archivedComment = repository.getOne(ARCHIVED_COMMENT_ID)
        archivedComment.archived

        expect:
        !commentService.findByTopic(IntId.one("why_you_little")).id.contains(archivedComment.id)
    }

    def "findByTopic throws when topic not found"() {
        when:
        commentService.findByTopic(NOT_EXISTING_TOPIC_ID)

        then:
        thrown(TopicNotFoundException)
    }
}
