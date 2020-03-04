plugins {
    kotlin("jvm") version "1.3.61"
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }

    apply(plugin = "kotlin")
    apply(plugin = "application")

    tasks {
        compileKotlin {
            kotlinOptions.jvmTarget = "11"
        }
        compileTestKotlin {
            kotlinOptions.jvmTarget = "11"
        }
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }
}
