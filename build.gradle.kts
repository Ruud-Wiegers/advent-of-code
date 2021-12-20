import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
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
            jvmTarget = "11"
            sourceCompatibility = "11"
            targetCompatibility = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
