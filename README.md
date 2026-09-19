[![Build](https://github.com/rodionovsasha/CommentService/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/rodionovsasha/CommentService/actions/workflows/build.yml)

[![Coverage Status](https://coveralls.io/repos/github/rodionovsasha/CommentService/badge.svg?branch=master)](https://coveralls.io/github/rodionovsasha/CommentService?branch=master)

[![License](https://img.shields.io/github/license/rodionovsasha/CommentService)](https://github.com/rodionovsasha/CommentService/blob/master/LICENSE)

# Comment Service
This is a demo project to show how JFixtures works on practice  
https://github.com/rodionovsasha/jfixtures

## Preparing a test data for integration tests
The main goal of this demo project to show how we can use JFixtures for populating relational databases with yml-based test data
 and test our services. There are some simple steps for using:
 * add maven dependency of JFixtures to your `pom.xml` file:
```code
    <dependency>
        <groupId>io.github.rodionovsasha</groupId>
        <artifactId>jfixtures</artifactId>
        <version>${jfixtures.version}</version>
        <scope>test</scope>
    </dependency>
 ```
 * put a yaml based test data to `resources` folder
 * convert the test data using JFixtures to a string or a file and execute generated SQL query:
```java
    import io.github.rodionovsasha.jfixtures.JFixtures;

    Path fixturesPath = Paths.get("src/test/resources/yaml/user")
    String sql = JFixtures
            .withConfig(fixturesPath.resolve('.conf.yml'))
            .load(fixturesPath)
            .compile()
            .toSql99()
            .toString()
    jdbcTemplate.execute(sql)
``` 
 * That's all! Now you can use it anywhere you need in the project.
 Please see test examples here: `CommentService/src/test/groovy/com/github/rodionovsasha/commentservice/services`

### JFixtures 3.0 examples

The examples in `src/test/resources/yaml/features` demonstrate the features added in JFixtures issues #24 and #74:

* `duplicate-primary-key.yml` contains two rows with the same explicitly defined `id`. Compiling it throws `ProcessorException` with both row names and the duplicated key value.
* `truncate-cascade.conf.yml` configures `clean_method: truncate_cascade`, which generates `TRUNCATE TABLE "users" CASCADE;` before inserting fixture rows.

The behavior is covered by `JFixturesFeaturesTest`.

### Build the application
<pre>
mvn clean install
</pre>
or using wrapper
<pre>
./mvnw clean install
</pre>

### Rest JSON API URL
<pre>
http://localhost:8080/v1/api
</pre>

### Run the application
<pre>
mvn spring-boot:run
</pre>
or using wrapper
<pre>
./mvnw spring-boot:run
</pre>
or simply run the application in your IDE using public static void main method.
