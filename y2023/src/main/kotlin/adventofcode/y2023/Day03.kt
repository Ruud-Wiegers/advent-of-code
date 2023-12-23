package adventofcode.y2023

import adventofcode.io.AdventSolution
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

    val numbers = "\\d+".toRegex().parse(input)
        .mapKeys { neighborhood(it.key, it.value.length) }
        .mapValues { it.value.toInt() }

    val symbols = "[^\\d.]".toRegex().parse(input)

    return Pair(numbers, symbols)
}


private fun Regex.parse(input: String): Map<Vec2, String> = input
    .lineSequence()
    .flatMapIndexed(this::parseLine)
    .toMap()

private fun Regex.parseLine(y: Int, line: String) =
    findAll(line).map { match -> Vec2(match.range.first, y) to match.value }
