import org.gradle.api.tasks.Exec

plugins {
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

group = "com.jkypch"
version = "0.0.1-SNAPSHOT"

// Gradle 8.14는 Java 25 toolchain을 지원하지 않으므로 직접 sourceCompatibility 지정
// 실제 컴파일은 설치된 JDK 25로 수행됨
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // JPA (Oracle)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.oracle.database.jdbc:ojdbc11:23.7.0.25.01")
    runtimeOnly("com.oracle.database.jdbc:ucp11:23.7.0.25.01")

    // MyBatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")

    // MongoDB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// ────────────────────────────────────────────
// Frontend build task
// ────────────────────────────────────────────
val frontendDir = file("frontend")
val frontendBuildDir = file("frontend/dist")
val staticDir = file("src/main/resources/static")

tasks.register<Exec>("frontendInstall") {
    workingDir = frontendDir
    commandLine("npm", "ci")
    inputs.file("frontend/package-lock.json")
    outputs.dir("frontend/node_modules")
}

tasks.register<Exec>("frontendBuild") {
    dependsOn("frontendInstall")
    workingDir = frontendDir
    commandLine("npm", "run", "build")
    inputs.dir("frontend/src")
    inputs.file("frontend/package.json")
    inputs.file("frontend/vite.config.ts")
    outputs.dir(frontendBuildDir)
}

tasks.register<Copy>("copyFrontend") {
    dependsOn("frontendBuild")
    from(frontendBuildDir)
    into(staticDir)
}

tasks.named("processResources") {
    dependsOn("copyFrontend")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
