import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters"))
}