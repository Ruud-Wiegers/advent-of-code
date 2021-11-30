plugins {
    base
    java
    kotlin("jvm")
    application
}

application {
    mainClass.set ("adventofcode.y2021.RunAllKt")
}

dependencies {
    implementation(project(":shared"))
}
