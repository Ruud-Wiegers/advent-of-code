plugins {
    base
    java
    kotlin("jvm")
    application
}

application {
    mainClass.set("adventofcode.y2021.RunAllKt")
}

dependencies {
    implementation(project(":shared"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}
