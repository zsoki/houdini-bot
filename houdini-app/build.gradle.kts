group = "hu.zsoki.houdinibot.app"
version = "0.1.0"

plugins {
    kotlin("plugin.serialization") version "1.3.61"
}

application {
    mainClassName = "hu.zsoki.houdinibot.app.HoudiniBotKt"
}

repositories {
    maven(url = "https://jitpack.io")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.3")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.7.8")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.0")

    // Discord
    implementation("com.serebit.strife", "strife-client-jvm", "0.3.1")

    // Persistence
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.0")
    testImplementation("io.mockk:mockk:1.9.3")
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
