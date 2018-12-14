import org.gradle.api.JavaVersion.VERSION_11

plugins {
    java
    maven
    id("org.springframework.boot") version "2.1.1.RELEASE"
    id("org.flywaydb.flyway") version "5.2.4"
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:1.18.4")
    compileOnly("org.projectlombok:lombok:1.18.4")
    compile("com.github.spotbugs:spotbugs-annotations:3.1.9")
    compile("org.flywaydb:flyway-core:5.2.3")
    compile("org.springframework.boot:spring-boot-starter-actuator:2.1.1.RELEASE")
    compile("org.springframework.boot:spring-boot-starter-data-jdbc:2.1.1.RELEASE")
    compile("org.springframework.boot:spring-boot-starter-data-rest:2.1.1.RELEASE")
    compile("org.springframework.data:spring-data-rest-hal-browser:3.1.3.RELEASE")
    runtime("com.zaxxer:HikariCP:3.2.0")
    runtime("org.hsqldb:hsqldb:2.4.1")
    testCompile("org.assertj:assertj-core:3.11.1")
    testCompile("org.hsqldb:sqltool:2.4.1")
    testCompile("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testCompile("org.junit.platform:junit-platform-runner:1.3.1")
    testCompile("org.springframework.boot:spring-boot-starter-test:2.1.1.RELEASE")
}

group = "hm.binkley.labs"
version = "0.0.1-SNAPSHOT"
description = "overrides"

configure<JavaPluginConvention> {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

flyway {
    url = "jdbc:hsqldb:file:$buildDir/overrides;create=true;shutdown=true"
    user = "sa"
}

defaultTasks("clean", "build")
