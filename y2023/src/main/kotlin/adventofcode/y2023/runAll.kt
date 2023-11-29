package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.formattedTime
import adventofcode.io.solve
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        listOf(
            Day01
        )
            .forEach(AdventSolution::solve)
    }.let { println(formattedTime(it)) }
}
