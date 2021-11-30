plugins {
    base
    java
    kotlin("jvm")
    application
}

application {
    mainClass.set( "adventofcode.y2020.RunAllKt")
}

dependencies {
    implementation(project(":shared"))
}
