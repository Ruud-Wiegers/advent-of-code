package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.util.cartesian

object Day02 : AdventSolution(2018, 2, "Inventory Management System") {

    override fun solvePartOne(input: String): Int {
        val frequencies = input.lines()
                .map { id ->
                    id.groupingBy { it }
                            .eachCount()
                            .values
                }

        val two = frequencies.count { 2 in it }
        val three = frequencies.count { 3 in it }

        return two * three
    }

    override fun solvePartTwo(input: String): String {
        val boxes = input.lines()
        val l = boxes[0].length
        return boxes
                .cartesian()
                .map { (a, b) -> a.filterIndexed { i, ch -> b[i] == ch } }
                .first { it.length == l - 1 }
    }
}
