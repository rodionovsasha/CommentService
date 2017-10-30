[![Build Status](https://travis-ci.org/rodionovsasha/CommentService.svg?branch=master)](https://travis-ci.org/rodionovsasha/CommentService)

# Comment Service
This is a demo project to show how JFixtures works on practice  
https://github.com/vkorobkov/jfixtures

## Preparing a test data for integration tests
The main goal of this demo project to show how we can use JFixtures for populating relational databases with yml-based test data
 and test our services. There are some simple steps for using:
 * add maven dependency of JFixtures to your `pom.xml` file:
```code
    <dependency>
        <groupId>com.github.vkorobkov</groupId>
        <artifactId>jfixtures</artifactId>
        <version>${jfixtures.version}</version>
        <scope>test</scope>
    </dependency>
 ```
 * put a yaml bases test data to `resources` folder
 * convert the test data using JFixtures to a string or a file and execute generated SQL query:
```java
    String sql = JFixtures.sql99("src/test/resources/yaml/user").asString()
    jdbcTemplate.execute(sql)
``` 
 * That's all! Now you can use it anywhere you need in the project.
 Please see test examples here: `CommentService/src/test/groovy/com/github/rodionovsasha/commentservice/services`

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
or simply run the application in your IDE using public static void main method.
