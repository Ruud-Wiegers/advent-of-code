plugins {
    kotlin("jvm") version "2.1.0"
}
allprojects {

    group = "nl.rwiegers"
    version = "1.0"

    repositories {
        mavenCentral()
    }


}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
