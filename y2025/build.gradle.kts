plugins {
    base
    java
    kotlin("jvm")
    application
}

application {
    mainClass.set("adventofcode.y2025.RunAllKt")
}

dependencies {
    implementation(project(":shared"))
    implementation("tools.aqua:z3-turnkey:4.14.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
}
