package adventofcode.y2025

import adventofcode.io.AdventSolution

fun main() {
    Day01.solve()
}

object Day01 : AdventSolution(2025, 1, "Secret Entrance") {

    override fun solvePartOne(input: String): Any {

        val input = parseInput(input)

        val scan = input.scan(50) { acc, i -> (acc + (i % 100 + 100)) % 100 }
        return scan.count { it <= 0 }
    }

    override fun solvePartTwo(input: String): Int {
        val input = parseInput(input)

        var count = 0

        input.fold(50) { acc, i ->
            var new = acc + i

            while (new >= 100) {
                new -= 100
                count++

            }
            while (new < 0) {
                new += 100
                count++
            }
            new
        }.let { println(it) }

        return count
    }
}

private fun parseInput(input: String) = input.lines()
    .map {
        val sign = if (it[0] == 'L') -1 else 1
        val step = it.substring(1).toInt()
        sign * step
    }