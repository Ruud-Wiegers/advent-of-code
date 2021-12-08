import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
}
allprojects {

    group = "nl.rwiegers"
    version = "1.0"

    repositories {
        jcenter()
        mavenCentral()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
}

subprojects {


    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "11"
            sourceCompatibility = "11"
            targetCompatibility = "11"
        }
    }
}
