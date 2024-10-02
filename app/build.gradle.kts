import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
	`java-library`
	java
	id("org.springframework.boot") version "3.3.5-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.6"
	id("checkstyle")
	jacoco
	id("com.github.johnrengelman.shadow") version "8.1.1"
	id("com.adarshr.test-logger") version "4.0.0"
	id("io.freefair.lombok") version "8.4"
	application
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.assertj:assertj-core:3.26.3")

	implementation("org.apache.commons:commons-lang3:3.14.0")
	implementation("org.apache.commons:commons-collections4:4.4")

	implementation("org.postgresql:postgresql:42.7.3")

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("jakarta.validation:jakarta.validation-api")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

testlogger {
	showFullStackTraces = true
	theme = ThemeType.MOCHA
}

tasks.jacocoTestReport {
	dependsOn(tasks.withType<Test>()) // tests are required to run before generating the report
	reports {
		xml.required = true
	}
}

application {
	mainClass.set("hexlet.code.app.AppApplication")
}
