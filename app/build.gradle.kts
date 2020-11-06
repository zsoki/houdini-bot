group = "hu.zsoki.houdinibot.app"
version = "0.1.0"

plugins {
    kotlin("plugin.serialization") version "1.4.10"
}

application {
    mainClassName = "hu.zsoki.houdinibot.app.HoudiniBotKt"
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")

    // Logging
    implementation("io.github.microutils:kotlin-logging:2.0.3")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.3")

    // Discord
    implementation("com.serebit.strife", "strife-client-jvm", "0.4.0")

    // Persistence
    implementation("org.xerial:sqlite-jdbc:3.32.3.2")
    implementation("org.jetbrains.exposed", "exposed-core", "0.22.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.22.1")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.22.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.1")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testImplementation("io.mockk:mockk:1.10.2")
}

tasks {
    register("stage") {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-XXLanguage:+InlineClasses")
        }
    }
    shadowJar {
        manifest {
            attributes["Main-Class"] = application.mainClassName
        }
        archiveBaseName.set("houdini-bot")
        archiveVersion.set("0.1.0")
    }
}
