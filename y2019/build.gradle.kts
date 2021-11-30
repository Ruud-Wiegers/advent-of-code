plugins {
    base
    java
    kotlin("jvm")
    application
}

application {
    mainClass.set( "adventofcode.y2019.RunAllKt")
}

dependencies {
    implementation(project(":shared"))
}
