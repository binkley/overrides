# Overrides

Demonstrate overrides from SQL into Spring Data, using Riot.JS for frontend

## Philosophy

* Use default as much as possible
* Write as little code as possible
* Follow best practices
* Use the right tool for the job

## Tech stack

* Spring Boot 2.1
* Spring Data JDBC (no JPA or Hibernate)
* RESTful repositories
* HSQLDB
* Flyway
* Riot JS
* Semantic UI CSS

## Command line

### Database

Tests and running the application rely on the database running.  Try this
(foreground process):

```
$ ./run-overrides.sh db
```

### Maven

Maven is fully supported.  Use `./mvnw` (`./mvnw.cmd` in a Windows shell).

### Gradle

Gradle is experimentally supported.  Use `./gradlew` (`./gradlew.bat` in a
Windows shell).

### Run script

Use `./run-overrides.sh` (no alternative in a Windows shell).  The `--help` flag
is useful.

## Examples

```sql
INSERT INTO OVERRIDES.p_Examples (name) VALUES ('Bob'), ('Nancy');
INSERT INTO OVERRIDES.p_Example_overrides (example_id, name) VALUES (0, 'Fred');
SELECT * FROM OVERRIDES.Examples;
```

After the inserts, try [the HAL browser](http://localhost:8080/examples).

## Caveats

* If you use IntelliJ IDEA, please see
[IDEA-204638](https://github.com/binkley/overrides/tree/IDEA-204638).
* Try out advice in (Spring Data JDBC, References, and Aggregates)
[https://spring.io/blog/2018/09/24/spring-data-jdbc-references-and-aggregates]
