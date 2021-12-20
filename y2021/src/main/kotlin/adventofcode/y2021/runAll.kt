package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.formattedTime
import adventofcode.solve
import kotlin.system.measureTimeMillis

fun main() {
    measureTimeMillis {
        listOf(
            Day01, Day02, Day03, Day04, Day05,
            Day06, Day07, Day08, Day09, Day10,
            Day11, Day12, Day13, Day14, Day15,
            Day16, Day17, Day18, Day19, Day20
        )
            .forEach(AdventSolution::solve)
    }.let { println(formattedTime(it)) }
}
