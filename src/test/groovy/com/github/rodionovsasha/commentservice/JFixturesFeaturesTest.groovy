package com.github.rodionovsasha.commentservice

import io.github.rodionovsasha.jfixtures.JFixtures
import io.github.rodionovsasha.jfixtures.processor.ProcessorException
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths

class JFixturesFeaturesTest extends Specification {
    private static final Path FIXTURES_PATH = Paths.get("src/test/resources/yaml/features")

    def "reports duplicate explicitly defined primary keys"() {
        when:
        JFixtures.noConfig()
                .load(FIXTURES_PATH.resolve("duplicate-primary-key.yml"))
                .compile()

        then:
        def exception = thrown(ProcessorException)
        exception.message == "Duplicate primary key [id=100500] in table [users]: rows [homer] and [bart] define the same value"
    }

    def "generates cascading truncate cleanup SQL"() {
        when:
        def sql = JFixtures
                .withConfig(FIXTURES_PATH.resolve("truncate-cascade.conf.yml"))
                .load(FIXTURES_PATH.resolve("truncate-cascade.yml"))
                .compile()
                .toSql99()
                .toString()

        then:
        sql.startsWith('TRUNCATE TABLE "users" CASCADE;\n')
    }
}
