import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
}
allprojects {

    group = "nl.rwiegers"
    version = "1.0"

    repositories {
        mavenCentral()
    }


}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "19"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
