package com.github.rodionovsasha.commentservice;

import com.github.rodionovsasha.commentservice.config.TestConfig;
import com.github.vkorobkov.jfixtures.JFixtures;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
public class UserTest {
    private static final String DATE_TIME_REGEXP = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{0,3}";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void shouldCreateUserWithTopicAndComment() {
        //Given
        val query = JFixtures.sql99("src/test/resources/yaml/user").asString();

        //When
        jdbcTemplate.update(query);

        //Then
        val user = jdbcTemplate.queryForList("SELECT * FROM user");
        assertEquals("{id=1, name=Bart, age=10, enabled=true}", user.get(0).toString());

        val topic = jdbcTemplate.queryForList("SELECT * FROM topic").get(0);

        assertEquals(1, topic.get("id"));
        assertEquals(1, topic.get("user_id"));
        assertEquals("some topic", topic.get("name"));
        assertTrue(topic.get("date").toString().matches(DATE_TIME_REGEXP));

        val comment = jdbcTemplate.queryForList("SELECT * FROM comment").get(0);

        assertEquals(10000, comment.get("id"));
        assertEquals(1, comment.get("user_id"));
        assertEquals(1, comment.get("topic_id"));
        assertEquals("some comment", comment.get("content"));
        assertTrue(comment.get("date").toString().matches(DATE_TIME_REGEXP));
    }
}