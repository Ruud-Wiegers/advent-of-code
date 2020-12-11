plugins {
    base
    java
    kotlin("jvm")
    application
}

application {
    mainClassName = "adventofcode.y2020.RunAllKt"
}

dependencies {
    implementation(project(":shared"))
}
