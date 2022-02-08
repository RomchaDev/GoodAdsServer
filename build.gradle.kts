import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.20-RC"
    application
    id("org.jetbrains.kotlin.plugin.noarg") version "1.3.31"
}

noArg {
    annotation("project_utils.NoArg")
    invokeInitializers = true
}

group = "me.admin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    //Spring
    implementation("org.springframework:spring-core:${Versions.SPRING}")
    implementation("org.springframework:spring-beans:${Versions.SPRING}")
    implementation("org.springframework:spring-context:${Versions.SPRING}")

    //Spring Boot
    implementation("org.springframework.boot:spring-boot:${Versions.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-starter-web:${Versions.SPRING_BOOT}")
    implementation("org.springframework.boot:spring-boot-devtools:${Versions.SPRING_BOOT}")

    //Database
    implementation("org.postgresql:postgresql:${Versions.POSTGRES}")
    implementation("org.hibernate:hibernate-core:${Versions.HIBERNATE}")

    implementation("com.google.code.gson:gson:${Versions.GSON}")

    implementation("org.brunocvcunha.instagram4j:instagram4j:${Versions.INSTAGRAM}")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

object Versions {
    const val SPRING = "5.3.15"
    const val SPRING_BOOT = "2.6.3"
    const val POSTGRES = "42.3.1"
    const val HIBERNATE = "5.6.4.Final"
    const val GSON = "2.8.9"
    const val LOMBOK = "1.5.20-RC"
    const val INSTAGRAM = "1.14"
    const val COROUTINES = "1.6.0"
}