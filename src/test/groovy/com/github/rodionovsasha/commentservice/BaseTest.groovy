package com.github.rodionovsasha.commentservice

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ContextConfiguration
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
@Transactional
class BaseTest extends Specification {
    final HOMER_ID = 1L
    final BART_ID = 2L
    final NOT_EXISTING_USER_ID = 999
    final TOPIC_ID = 1
    final NOT_EXISTING_TOPIC_ID = 99
    final ARCHIVED_TOPIC_ID = 7
    final COMMENT_ID = 1L
    final NOT_EXISTING_COMMENT_ID = 9999
}
