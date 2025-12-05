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
            if (acc == 0 && i < 0) count--
            if (new == 0 && i < 0) count++
            new
        }

        return count
    }
}

private fun parseInput(input: String) = input
    .replace('L', '-')
    .replace('R', '+')
    .lines()
    .map { it.toInt() }