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
}
