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
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}
