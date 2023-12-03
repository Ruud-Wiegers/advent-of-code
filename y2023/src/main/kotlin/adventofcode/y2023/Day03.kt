package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Vec2

fun main() {
    Day03.solve()
}

object Day03 : AdventSolution(2023, 3, "Gear Ratios") {

    override fun solvePartOne(input: String): Int {
        val (numbers, symbols) = parse(input)

        return numbers.filterKeys { it.any(symbols::containsKey) }
            .values
            .sum()
    }

    override fun solvePartTwo(input: String): Int {
        val (numbers, symbols) = parse(input)

        val gears = symbols.filterValues { it == "*" }.keys


        return gears
            .map { position -> numbers.filterKeys { position in it }.values }
            .filter { it.size == 2 }
            .sumOf { it.reduce(Int::times) }

    }
}


private fun neighborhood(root: Vec2, length: Int) =
    (root.y - 1..root.y + 1).flatMap { y ->
        (root.x - 1..root.x + length).map { x -> Vec2(x, y) }
    }.toSet()


private fun parse(input: String): Pair<Map<Set<Vec2>, Int>, Map<Vec2, String>> {

    val numbers = mutableMapOf<Set<Vec2>, Int>()
    val symbols = mutableMapOf<Vec2, String>()

    input.lines().forEachIndexed { y, line ->

        "\\d+".toRegex().findAll(line).forEach { match ->
            val root = Vec2(match.range.first, y)
            val neighborhood = neighborhood(root, match.value.length)
            numbers[neighborhood] = match.value.toInt()
        }

        "[^\\d.]".toRegex().findAll(line).forEach { match ->
            symbols[Vec2(match.range.first, y)] = match.value
        }
    }

    return Pair(numbers, symbols)
}


