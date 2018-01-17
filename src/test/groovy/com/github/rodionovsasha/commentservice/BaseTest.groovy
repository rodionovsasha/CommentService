package com.github.rodionovsasha.commentservice

import com.github.vkorobkov.jfixtures.IntId
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

@ContextConfiguration
@TestPropertySource("classpath:test.properties")
@SpringBootTest
@Transactional
class BaseTest extends Specification {
    final HOMER_ID = IntId.one("homer")
    final BART_ID = IntId.one("bart")
    final NOT_EXISTING_USER_ID = 999
    final TOPIC_ID = IntId.one("stupid_flanders")
    final NOT_EXISTING_TOPIC_ID = 99
    final ARCHIVED_TOPIC_ID = IntId.one("woo_hoo")
    final COMMENT_ID = IntId.one("comment1")
    final NOT_EXISTING_COMMENT_ID = 9999
    final ARCHIVED_COMMENT_ID = IntId.one("archived_comment")
}
