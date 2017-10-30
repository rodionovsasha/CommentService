package com.github.rodionovsasha.commentservice

import com.github.vkorobkov.jfixtures.IntId
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
    final HOMER_ID = IntId.one("homer") as Long
    final BART_ID = IntId.one("bart") as Long
    final NOT_EXISTING_USER_ID = 999
    final TOPIC_ID = IntId.one("stupid_flanders") as Long
    final NOT_EXISTING_TOPIC_ID = 99
    final ARCHIVED_TOPIC_ID = IntId.one("woo_hoo") as Long
    final COMMENT_ID = IntId.one("comment1") as Long
    final NOT_EXISTING_COMMENT_ID = 9999
    final ARCHIVED_COMMENT_ID = IntId.one("archived_comment") as Long
}
