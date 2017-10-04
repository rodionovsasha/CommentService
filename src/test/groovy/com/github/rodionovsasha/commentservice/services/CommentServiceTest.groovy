package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.ArchivedTopicException
import com.github.rodionovsasha.commentservice.exceptions.CommentAccessException
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import com.github.rodionovsasha.commentservice.repositories.CommentRepository
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
            id instanceof Long
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
        commentService.update(2L, BART_ID, COMMENT_CONTENT)

        then:
        thrown(InactiveUserException)
    }

    def "update throws when user not found"() {
        when:
        commentService.update(2L, NOT_EXISTING_USER_ID, COMMENT_CONTENT)

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
        commentService.update(2L, HOMER_ID, COMMENT_CONTENT)

        then:
        def e = thrown(CommentAccessException)
        e.message == "Non-comment owner with id '" + HOMER_ID + "' is trying to update the comment"
    }
}
