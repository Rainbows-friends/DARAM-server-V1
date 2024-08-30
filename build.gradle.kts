plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24" apply true
    kotlin("plugin.spring") version "1.9.24"
}

group = "Rainbow_Frends"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    /* Spring Boot Starters */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-tomcat")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    /* Kotlin Support */
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    /* OpenAPI/Swagger */
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    /* GAuth */
    implementation("com.github.YangSiJun528:GAuth-spring-boot-starter:3.0.0")
    implementation("com.github.GSM-MSG:GAuth-SDK-Java:v3.0.0")

    /* Thymeleaf Extras */
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

    /* JWT */
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    /* AWS */
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.691")

    /* Database */
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    /* Testing */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}