group = "hu.zsoki.houdinibot.app"
version = "0.1.0"

application {
    mainClassName = "hu.zsoki.houdinibot.app.HoudiniBotKt"
}

repositories {
    maven(url = "https://jitpack.io")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")

    // Logging
    implementation("io.github.microutils:kotlin-logging:2.0.6")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")

    // Discord
    implementation("dev.kord:kord-core:kotlin-1.5") {
        version {
            strictly("kotlin-1.5-SNAPSHOT")
        }
    }

    // Persistence
    implementation("org.xerial:sqlite-jdbc:3.34.0")
    implementation("org.jetbrains.exposed", "exposed-core", "0.31.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.31.1")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.31.1")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.2")
    testImplementation("io.mockk:mockk:1.11.0")
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
