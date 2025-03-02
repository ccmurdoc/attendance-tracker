

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"

}

application {
    mainClass.set("com.example.ApplicationKt")
}

val kotlinVersion = "1.8.10"
val ktorVersion = "2.3.0"
val logback_version = "1.2.11"
val exposed_version = "0.43.0"
val sqlite_version = "3.36.0"
val rabbitmq_version = "5.16.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("org.xerial:sqlite-jdbc:3.36.0")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("com.rabbitmq:amqp-client:5.16.0")
    implementation("org.jetbrains.exposed:exposed-core:0.43.0")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("org.jetbrains.exposed:exposed-dao:0.43.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.43.0")
    //implementation("io.ktor:ktor-server-routing:$ktorVersion")
    implementation("com.rabbitmq:amqp-client:5.16.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.ktor:ktor-server-metrics-micrometer:2.3.0")
    implementation("io.micrometer:micrometer-registry-prometheus:1.11.0")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
kotlin {
    jvmToolchain(17)
}

tasks.create("stage") {
    dependsOn("installDist", "shadowJar")
}