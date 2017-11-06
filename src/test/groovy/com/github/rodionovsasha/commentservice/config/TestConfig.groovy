package com.github.rodionovsasha.commentservice.config

import com.github.vkorobkov.jfixtures.JFixtures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.jdbc.core.JdbcTemplate

@Configuration
@EnableAutoConfiguration
@EnableSpringDataWebSupport
@ComponentScan
class TestConfig implements CommandLineRunner {
    @Autowired
    JdbcTemplate jdbcTemplate

    @Override
    void run(String... strings) throws Exception {
        String sql = JFixtures.sql99("src/test/resources/yaml/user").asString()
        jdbcTemplate.execute(sql)
    }
}
