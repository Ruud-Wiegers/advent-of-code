import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.21"
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
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_19)
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
