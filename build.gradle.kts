import com.github.spotbugs.SpotBugsTask
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.gradle.api.JavaVersion.VERSION_11
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import java.math.BigDecimal

plugins {
    java
    maven
    id("com.dorongold.task-tree") version "1.3.1"
    id("org.springframework.boot") version "2.1.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("org.flywaydb.flyway") version "5.2.4"
    id("org.unbroken-dome.test-sets") version "2.0.3"
    id("com.github.spotbugs") version "1.6.8"
    jacoco
}

testSets {
    "databaseTest"()
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

// TODO: Cleaner way to ensure fast failure?
tasks["databaseTest"].mustRunAfter(tasks.test)

tasks.withType<JacocoReportBase> {
    executionData(fileTree(buildDir).include("/jacoco/*.exec"))
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

tasks {
    wrapper {
        distributionType = ALL
    }

    clean {
        delete("$projectDir/out")
    }

    jacocoTestReport {
        dependsOn(test, "databaseTest")
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = BigDecimal.valueOf(0.50)
                }
            }
        }
        dependsOn(jacocoTestReport)
    }

    check {
        dependsOn(jacocoTestCoverageVerification)
    }
}
