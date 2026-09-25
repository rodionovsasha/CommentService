[![Build](https://github.com/rodionovsasha/CommentService/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/rodionovsasha/CommentService/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/rodionovsasha/CommentService/badge.svg?branch=master)](https://coveralls.io/github/rodionovsasha/CommentService?branch=master)
[![License](https://img.shields.io/github/license/rodionovsasha/CommentService)](https://github.com/rodionovsasha/CommentService/blob/master/LICENSE)

# Comment Service

This project is a working example of using [JFixtures](https://github.com/rodionovsasha/jfixtures) to prepare relational-database data for integration tests. JFixtures reads YAML fixtures, resolves identifiers and relations, and produces SQL that can be executed before a test.

The project uses JFixtures **3.1.0**. Its executable examples live in `src/test/resources/yaml`, and their checks are in `src/test/groovy/com/github/rodionovsasha/commentservice`.

## What JFixtures does

JFixtures gives every fixture row a readable label, such as `homer` or `stupid_flanders`. It derives a deterministic primary-key value for that label unless an `id` is supplied explicitly. Other fixtures can then refer to the row by label instead of hard-coding database IDs.

The usual workflow is:

1. Describe rows in one or more YAML files.
2. Optionally describe relationships, generated columns, cleanup, and other rules in a YAML configuration file.
3. Load and compile the fixture set with JFixtures.
4. Render SQL with `toSql99()` or apply the compiled fixtures to a JDBC connection or `DataSource`.

This keeps fixture data readable and makes foreign keys stable when fixtures are reorganized.

## Add the dependency

JFixtures is already configured for tests in this project's [pom.xml](pom.xml):

```xml
<dependency>
    <groupId>io.github.rodionovsasha</groupId>
    <artifactId>jfixtures</artifactId>
    <version>${jfixtures.version}</version>
    <scope>test</scope>
</dependency>
```

Keep the dependency in test scope when fixtures are used only to set up tests. This project defines `${jfixtures.version}` as `3.1.0`.

## Create a fixture set

Put fixtures under `src/test/resources`. A directory is convenient for a related group of tables. In this repository, `src/test/resources/yaml/user` contains `user.yml`, `topic.yml`, and `comment.yml`.

Each YAML file represents a table; the file name without `.yml` is the table name. The top-level keys are row labels and their values are column maps:

```yaml
# src/test/resources/yaml/user/user.yml
homer:
  name: Homer
  age: 39

bart:
  name: Bart
  age: 10
  active: false
```

The example creates rows in the `user` table. `homer` and `bart` are fixture labels, not values inserted into a column. JFixtures generates the primary keys for them. Application tests can obtain the generated integer ID through the same label:

```groovy
import io.github.rodionovsasha.jfixtures.IntId

def homerId = IntId.one("homer")
```

That is how `BaseTest.groovy` refers to the users, topics, and comments prepared by this fixture set. Use a label that is unique within the table and keep it stable so test assertions stay readable.

To use an explicit primary key instead, include the key column in the row. JFixtures validates explicitly repeated primary keys and reports both conflicting labels:

```yaml
users:
  homer:
    id: 1
    name: Homer
  bart:
    id: 2
    name: Bart
```

## Define foreign-key references

The `.conf.yml` file alongside a fixture directory configures how label values become foreign keys. In this project, the configuration maps `topic.owner_id` to a `user` label, and both comment foreign keys to their target tables:

```yaml
# src/test/resources/yaml/user/.conf.yml
refs:
  topic:
    owner_id: user
  comment:
    user_id: user
    topic_id: topic
```

With that configuration, a topic can refer to `homer` and a comment can refer to both a user and a topic:

```yaml
# topic.yml
stupid_flanders:
  title: Stupid Flanders
  owner_id: homer

# comment.yml
comment1:
  content: Why you little...!
  user_id: homer
  topic_id: stupid_flanders
```

JFixtures replaces `homer` with the generated primary key of the `user` row and `stupid_flanders` with the generated primary key of the `topic` row. It also orders the generated statements so referenced rows are inserted first.

For a self-reference, map the column to the same table name. The ordered-map example in `src/test/resources/yaml/features/new-features/ordered` uses this configuration:

```yaml
refs:
  categories:
    parent_id: categories
```

If parent rows must be emitted before child rows, preserve their order with YAML's `!omap`:

```yaml
!omap
- grandparent: { name: Grandparent }
- parent: { name: Parent, parent_id: grandparent }
```

## Use SQL expressions and shared column rules

Ordinary YAML values become SQL literals. Prefix an expression with `sql:` when it must be emitted as SQL instead. For example, the following creates a timestamp at execution time:

```yaml
stupid_flanders_comment:
  content: Eat My Shorts!
  date: sql:CURRENT_TIMESTAMP() + 4
  user_id: bart
  topic_id: stupid_flanders
```

The project also uses a reusable column concern to add a creation date to every `topic` and `comment` row that does not set `date` itself:

```yaml
columns:
  concerns:
    add_creation_date:
      date: sql:CURRENT_TIMESTAMP()
  apply:
    tables_with_date:
      to: topic, comment
      concerns: add_creation_date
```

An explicit row value takes precedence, so a row can override the shared rule as shown in the preceding example.

## Load, compile, and execute fixtures

The following Java setup is equivalent to the Spring test configuration in this project. It loads all `.yml` files under the fixture directory, applies `.conf.yml`, generates SQL-99, and runs it through Spring's `JdbcTemplate`:

```java
import io.github.rodionovsasha.jfixtures.JFixtures;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.jdbc.core.JdbcTemplate;

Path fixturesPath = Paths.get("src/test/resources/yaml/user");

String sql = JFixtures
        .withConfig(fixturesPath.resolve(".conf.yml"))
        .load(fixturesPath)
        .compile()
        .toSql99()
        .toString();

jdbcTemplate.execute(sql);
```

For a fixture set that does not need configuration, start with `noConfig()`:

```java
String sql = JFixtures.noConfig()
        .load(Paths.get("src/test/resources/yaml/features/values"))
        .compile()
        .toSql99()
        .toString();
```

You can skip creating a SQL string when a JDBC target is available. The following variants are supported:

```java
// Applies the loaded fixture set directly to a JDBC connection.
JFixtures.noConfig().load(fixturesPath).apply(connection);

// Applies compiled fixtures to a DataSource.
JFixtures.noConfig().load(fixturesPath).compile().apply(dataSource);

// Applies the rendered SQL form to a DataSource.
JFixtures.noConfig().load(fixturesPath).compile().toSql99().apply(dataSource);
```

Use the SQL-string form when it is useful to log, inspect, or execute the generated script through an existing test utility. Use `apply(...)` when the test owns the connection or data source.

## Control cleanup and insertion order

JFixtures emits cleanup statements before inserts. Configure a table when it needs a different cleanup strategy, prerequisites, timestamps, or primary-key behavior:

```yaml
clean_tables: [orphaned]
tables:
  orphaned:
    applies_to: orphaned
    clean_method: truncate
  users:
    applies_to: users
    requires: [roles]
    timestamps:
      enabled: true
      value: "sql:CURRENT_TIMESTAMP"
```

`clean_tables` includes a table even if it has no fixture rows. `clean_method: truncate` produces a `TRUNCATE TABLE` statement; `truncate_cascade` is available for databases that support cascading truncation:

```yaml
tables:
  cascade_cleanup:
    applies_to: users
    clean_method: truncate_cascade
```

The example produces `TRUNCATE TABLE "users" CASCADE;`. `requires: [roles]` ensures `roles` is inserted before `users`. Timestamp support adds the configured timestamp value to the standard audit columns of each matching row.

## Useful JFixtures 3.1 features

The feature fixtures in `src/test/resources/yaml/features/new-features` are executable documentation for additional capabilities:

| Need | Configuration or fixture example |
| --- | --- |
| Use UUID, string, or long primary keys | `options.conf.yml` and `advanced.conf.yml` configure `id_generator` or `pk.type`. |
| Reference a non-primary-key column | `advanced.conf.yml` maps `comments.author_public_id` to `users.public_id`; the value `alice` resolves the row label. |
| Inline reference to a specific column | `users:alice:public_id` in `advanced/comments.yml`. |
| Composite primary keys and references | `composite-template.conf.yml`. |
| Generate rows from a template | `composite-template/users.yml`, enabled with `templates.enabled: true`. |
| Many-to-many and polymorphic relations | `associations.conf.yml`. |
| Named configuration profiles | `profiles.conf.yml`, selected with `withProfile("integration")`. |
| SQL hooks before or after fixture processing | `hooks.conf.yml` plus the `hooks` directory. |
| YAML anchors and binary/date values | `anchors/users.yml` and `values/events.yml`. |

For example, select the default or a named profile after loading the same fixture data:

```java
var fixtures = JFixtures
        .withConfig(Paths.get("src/test/resources/yaml/features/new-features/profiles.conf.yml"))
        .load(Paths.get("src/test/resources/yaml/features/new-features/profiles"));

String defaultSql = fixtures.withDefaultProfile().compile().toSql99().toString();
String integrationSql = fixtures.withProfile("integration").compile().toSql99().toString();
```

The supplied configuration defines `clean_method: none` for the default profile and `clean_method: truncate` for the integration profile.

## Troubleshooting checklist

* Ensure every YAML file name matches the actual database table name. A wrong table name produces SQL for the wrong table.
* Add each foreign-key column to `refs` and use the target row label as its YAML value. Do not use a label where the database expects an arbitrary numeric ID.
* Specify `id` only when an explicit value is needed. Two labels with the same explicit primary key fail compilation with `ProcessorException`.
* Use `sql:` for database functions and expressions. Without it, `CURRENT_TIMESTAMP()` is treated as a text value.
* Render and inspect `toSql99().toString()` when debugging ordering, cleanup, or reference resolution.
* Keep fixture execution inside the test setup transaction when the test framework rolls transactions back; this prevents data from leaking between tests.

### Build the application

```text
mvn clean install
```

or using the wrapper:

```text
./mvnw clean install
```

### REST JSON API URL

```text
http://localhost:8080/v1/api
```

### Run the application

```text
mvn spring-boot:run
```

or using the wrapper:

```text
./mvnw spring-boot:run
```

You can also run the application's `main` method from an IDE.
