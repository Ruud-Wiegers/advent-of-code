import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.20"
}
allprojects {

    group = "nl.rwiegers"
    version = "1.0"

    repositories {
        jcenter()
    }
}

dependencies {
    implementation(kotlin("stdlib"))
}

subprojects {


    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            sourceCompatibility = "11"
            targetCompatibility = "11"
            freeCompilerArgs += "-Xopt-in=kotlin.ExperimentalStdlibApi,kotlin.time.ExperimentalTime"
        }
    }
}
