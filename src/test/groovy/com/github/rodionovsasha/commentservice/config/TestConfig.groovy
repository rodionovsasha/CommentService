package com.github.rodionovsasha.commentservice.config

import com.github.vkorobkov.jfixtures.JFixtures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate

import java.nio.file.Path
import java.nio.file.Paths

@Configuration
@EnableAutoConfiguration
@ComponentScan
class TestConfig implements CommandLineRunner {
    @Autowired
    JdbcTemplate jdbcTemplate

    @Override
    void run(String... strings) throws Exception {
        Path fixturesPath = Paths.get("src/test/resources/yaml/user")
        String sql = JFixtures
                .withConfig(fixturesPath.resolve('.conf.yml'))
                .load(fixturesPath)
                .compile()
                .toSql99()
                .toString()
        jdbcTemplate.execute(sql)
    }
}
