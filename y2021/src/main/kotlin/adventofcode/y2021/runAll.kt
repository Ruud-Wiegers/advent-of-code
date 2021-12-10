package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.formattedTime
import adventofcode.solve
import kotlin.system.measureTimeMillis

fun main() {
    repeat(3) {
        measureTimeMillis {
            listOf(
                Day01, Day02, Day03, Day04, Day05,
                Day06, Day07, Day08, Day09, Day10
            )
                .forEach(AdventSolution::solve)
        }.let { println(formattedTime(it)) }
    }
}
