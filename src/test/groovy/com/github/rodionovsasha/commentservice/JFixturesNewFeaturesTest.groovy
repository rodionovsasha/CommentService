package com.github.rodionovsasha.commentservice

import io.github.rodionovsasha.jfixtures.IntId
import io.github.rodionovsasha.jfixtures.JFixtures
import io.github.rodionovsasha.jfixtures.LongId
import io.github.rodionovsasha.jfixtures.Shortcuts
import io.github.rodionovsasha.jfixtures.StringId
import io.github.rodionovsasha.jfixtures.UuidId
import io.github.rodionovsasha.jfixtures.sql.SqlFormatting
import org.h2.jdbcx.JdbcDataSource
import spock.lang.Specification

import java.nio.file.Path
import java.nio.file.Paths
import java.sql.DriverManager

class JFixturesNewFeaturesTest extends Specification {
    private static final Path FIXTURES_PATH = Paths.get("src/test/resources/yaml/features/new-features")

    def "resolves inline and configured references, labels, and UUID primary keys"() {
        when:
        def sql = compile("advanced", "advanced.conf.yml")

        then:
        sql.contains("'${UuidId.one("alice")}'")
        sql.contains("'alice-public'")
        sql.contains("'comment-welcome'")
        sql.contains(IntId.one("alice").toString())
        sql.contains("'${UuidId.one("bob")}'")
        sql.contains("'bob-public'")
    }

    def "expands polymorphic and many-to-many associations"() {
        when:
        def sql = compile("associations", "associations.conf.yml")

        then:
        sql.contains('"eater_id", "eater_type"')
        sql.contains("'Monkey'")
        !sql.contains('"eater")')
        !sql.contains('"tags")')
        sql.count('INSERT INTO "posts_tags"') == 2
        sql.contains(IntId.one("blue").toString())
        sql.contains(IntId.one("green").toString())
    }

    def "creates composite keys and expands opt-in fixture templates"() {
        when:
        def sql = compile("composite-template", "composite-template.conf.yml")

        then:
        sql.contains('"tenant_id", "order_id", "description"')
        sql.contains(IntId.one("spring_sale.tenant_id").toString())
        sql.contains(IntId.one("spring_sale.order_id").toString())
        sql.contains('"order_tenant_id", "order_number", "sku"')
        sql.count('INSERT INTO "users"') == 3
        sql.contains("'User 3'")
        sql.contains(', 30)')
    }

    def "uses configured cleanup, ordering, timestamps, generators, and computed key columns"() {
        when:
        def sql = compile("options", "options.conf.yml")

        then:
        sql.contains('TRUNCATE TABLE "orphaned";')
        sql.indexOf('INSERT INTO "roles"') < sql.indexOf('INSERT INTO "users"')
        sql.contains('"created_at", "created_on", "updated_at", "updated_on"')
        sql.count('CURRENT_TIMESTAMP') == 4
        sql.contains("'${StringId.one("alice")}'")
        sql.contains('"AUDIT_LOG_ID"')
        sql.contains("'${StringId.one("created")}'")
        sql.contains(LongId.one("entry").toString())
    }

    def "accepts YAML anchors and preserves explicit ordered maps"() {
        when:
        def anchorSql = compile("anchors")
        def orderedSql = compile("ordered", "ordered.conf.yml")

        then:
        anchorSql.count('INSERT INTO "users"') == 1
        anchorSql.contains('"active", "name"')
        anchorSql.contains('TRUE')
        orderedSql.indexOf("'Grandparent'") < orderedSql.indexOf("'Parent'")
        orderedSql.contains(IntId.one("grandparent").toString())
    }

    def "renders date and binary YAML values"() {
        when:
        def sql = compile("values")

        then:
        sql.contains("'2026-09-20'")
        sql.contains("X'48656c6c6f'")
    }

    def "provides shortcut and configurable SQL formatting APIs"() {
        when:
        def shortcutSql = Shortcuts.Str.sql99(FIXTURES_PATH.resolve("values").toString())
        def configuredShortcutSql = Shortcuts.Str.sql99(
                FIXTURES_PATH.resolve("profiles").toString(),
                FIXTURES_PATH.resolve("profiles.conf.yml").toString()
        )
        def formattedSql = JFixtures.noConfig()
                .load(FIXTURES_PATH.resolve("values"))
                .compile()
                .toSql99()
                .withFormatting(new SqlFormatting("\r\n", 1))
                .toString()

        then:
        shortcutSql.contains('INSERT INTO "events"')
        !configuredShortcutSql.contains('DELETE FROM "users"')
        formattedSql.contains("\r\n\r\nINSERT INTO")
    }

    def "uses default and named configuration profiles"() {
        given:
        def fixtures = JFixtures.withConfig(FIXTURES_PATH.resolve("profiles.conf.yml"))
                .load(FIXTURES_PATH.resolve("profiles"))

        expect:
        !fixtures.withDefaultProfile().compile().toSql99().toString().contains('DELETE FROM "users"')
        fixtures.withProfile("integration").compile().toSql99().toString().contains('TRUNCATE TABLE "users";')
    }

    def "runs configured and directory SQL hooks in fixture-set order"() {
        when:
        def sql = compile("hooks", "hooks.conf.yml")

        then:
        sql.indexOf("directory before") < sql.indexOf("before all")
        sql.indexOf("before all") < sql.indexOf("before cleanup users")
        sql.indexOf("before cleanup users") < sql.indexOf("before inserts users")
        sql.indexOf("before inserts users") < sql.indexOf('INSERT INTO "users"')
        sql.indexOf('INSERT INTO "users"') < sql.indexOf("after inserts users")
        sql.indexOf("after inserts users") < sql.indexOf("after all")
        sql.indexOf("after all") < sql.indexOf("directory after")
    }

    def "accepts map fixtures with an empty table"() {
        when:
        def sql = JFixtures.noConfig().addTables([
                users: [alice: [name: "Alice"]],
                empty_table: []
        ]).compile().toSql99().toString()

        then:
        sql.contains('INSERT INTO "users"')
        sql.contains('DELETE FROM "empty_table";')
        !sql.contains('INSERT INTO "empty_table"')
    }

    def "applies compiled fixtures through connection, result, and SQL result APIs"() {
        given:
        def connectionUrl = "jdbc:h2:mem:jfixtures_connection;DB_CLOSE_DELAY=-1"
        def connection = DriverManager.getConnection(connectionUrl)
        connection.createStatement().execute('CREATE TABLE "users" ("id" INT PRIMARY KEY, "name" VARCHAR(50))')
        def resultDataSource = new JdbcDataSource(URL: "jdbc:h2:mem:jfixtures_result;DB_CLOSE_DELAY=-1")
        resultDataSource.connection.createStatement().execute('CREATE TABLE "users" ("id" INT PRIMARY KEY, "name" VARCHAR(50))')
        def sqlResultDataSource = new JdbcDataSource(URL: "jdbc:h2:mem:jfixtures_sql_result;DB_CLOSE_DELAY=-1")
        sqlResultDataSource.connection.createStatement().execute('CREATE TABLE "users" ("id" INT PRIMARY KEY, "name" VARCHAR(50))')

        when:
        JFixtures.noConfig().addTables([users: [alice: [name: "Alice"]]]).apply(connection)
        JFixtures.noConfig().addTables([users: [bob: [name: "Bob"]]]).compile().apply(resultDataSource)
        JFixtures.noConfig().addTables([users: [carol: [name: "Carol"]]]).compile().toSql99().apply(sqlResultDataSource)

        then:
        rowName(connection) == "Alice"
        rowName(resultDataSource.connection) == "Bob"
        rowName(sqlResultDataSource.connection) == "Carol"

        cleanup:
        connection.close()
    }

    private static String compile(String directory, String config = null) {
        def fixtures = config == null
                ? JFixtures.noConfig()
                : JFixtures.withConfig(FIXTURES_PATH.resolve(config))
        fixtures.load(FIXTURES_PATH.resolve(directory)).compile().toSql99().toString()
    }

    private static String rowName(def connection) {
        def result = connection.createStatement().executeQuery('SELECT "name" FROM "users"')
        result.next()
        result.getString(1)
    }
}
