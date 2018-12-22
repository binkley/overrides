# Overrides

Demonstrate overrides from SQL into Spring Data

## Tech stack

* Spring Boot 2.1
* Spring Data JDBC (no JPA or Hibernate)
* RESTful repositories
* HSQLDB
* Flyway
* Riot JS
* Semantic UI CSS

## Command line

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

If you use IntelliJ IDEA, please see
[IDEA-204638](https://github.com/binkley/overrides/tree/IDEA-204638).
