package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve

fun main(args: Array<String>) {
    repeat(5) {
        listOf(Day01, Day02, Day03, Day04, Day05, Day06, Day07, Day08, Day09, Day10, Day11)
                .forEach(AdventSolution::solve)
    }
}
