import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion.VERSION_11
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import com.github.spotbugs.SpotBugsTask

plugins {
    java
    maven
    id("org.springframework.boot") version "2.1.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("org.flywaydb.flyway") version "5.2.4"
    id("com.github.spotbugs") version "1.6.8"
}

dependencyManagement {
    imports {
        mavenBom(BOM_COORDINATES)
    }
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    compile("com.github.spotbugs:spotbugs-annotations:3.1.10")
    compile("org.flywaydb:flyway-core")
    compile("org.springframework.boot:spring-boot-starter-actuator")
    compile("org.springframework.boot:spring-boot-starter-data-jdbc")
    compile("org.springframework.boot:spring-boot-starter-data-rest")
    compile("org.springframework.data:spring-data-rest-hal-browser")
    runtime("com.zaxxer:HikariCP")
    runtime("org.hsqldb:hsqldb")
    testCompile("org.assertj:assertj-core")
    testCompile("org.junit.jupiter:junit-jupiter-engine")
    testCompile("org.springframework.boot:spring-boot-starter-test")
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

tasks.withType<Test> {
    useJUnitPlatform()
}

spotbugs {
    toolVersion = "3.1.10"
}

tasks.withType<SpotBugsTask> {
    reports {
        xml.isEnabled = false
        html.isEnabled = true
    }
}

flyway {
    url = "jdbc:hsqldb:hsql://localhost/overrides"
    user = "sa"
}

defaultTasks("clean", "build")
