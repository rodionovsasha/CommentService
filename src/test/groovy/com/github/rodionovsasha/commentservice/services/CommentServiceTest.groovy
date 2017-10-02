package com.github.rodionovsasha.commentservice.services

import com.github.rodionovsasha.commentservice.BaseTest
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException
import org.springframework.beans.factory.annotation.Autowired

class CommentServiceTest extends BaseTest {
    @Autowired
    CommentService commentService
    final HOMER_ID = 1
    final TOPIC_ID = 1
    final COMMENT_CONTENT = "comment"
    final NOT_EXISTING_USER_ID = 999
    final NOT_EXISTING_TOPIC_ID = 99

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
        commentService.add(COMMENT_CONTENT, TOPIC_ID, 2)

        then:
        thrown(InactiveUserException)
    }

    def "add throws when user not found"() {
        when:
        commentService.add(COMMENT_CONTENT, TOPIC_ID, NOT_EXISTING_USER_ID)

        then:
        thrown(UserNotFoundException)
    }

    def "add throws when topic not found"() {
        when:
        commentService.add(COMMENT_CONTENT, NOT_EXISTING_TOPIC_ID, HOMER_ID)

        then:
        thrown(TopicNotFoundException)
    }
}
