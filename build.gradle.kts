import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.CleanDataTask

object Versions {
    const val BotVersion = "1.0.0"
    const val Kord = "0.7.4"
    const val LatexMath = "1.0.7"
    const val Slf4j = "1.7.32"
    const val Datetime = "0.2.1"
    const val Ktor = "1.6.2"
}

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.5.20"
    kotlin("plugin.serialization") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("dev.kord:kord-core:${Versions.Kord}")
    implementation("org.scilab.forge:jlatexmath:${Versions.LatexMath}")
    implementation("org.slf4j:slf4j-simple:${Versions.Slf4j}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Datetime}")
    implementation("io.ktor:ktor-client-core:${Versions.Ktor}")
    implementation("io.ktor:ktor-client-cio:${Versions.Ktor}")

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

application {
    // Define the main class for the application.
    mainClass.set("me.palander.omega.MainKt")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveFileName.set("Omega-${Versions.BotVersion}.jar")
    manifest {
        attributes("Main-Class" to "me.palander.omega.MainKt")
    }
}

tasks.register<Copy>("bin") {
    dependsOn("shadowJar")
    from("${rootDir.path}/build/libs/Omega-${Versions.BotVersion}.jar")
    into("bin")
}

// Extend the clean task to also delete the bin directory
tasks.clean {
    doFirst {
        File("bin").deleteRecursively()
    }
}