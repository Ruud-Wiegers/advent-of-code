package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2

fun main() {
    Day03.solve()
}

object Day03 : AdventSolution(2024, 3, "Mull It Over") {

    override fun solvePartOne(input: String): Int = Regex("""mul\((\d+),(\d+)\)""")
        .findAll(input)
        .map(MatchResult::destructured)
        .sumOf { (a, b) -> a.toInt() * b.toInt() }

    override fun solvePartTwo(input: String): Int {
        var enabled = true
        var sum = 0
        """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex().findAll(input).forEach { result ->
            when {
                result.value == "do()" -> enabled = true
                result.value == "don't()" -> enabled = false
                enabled -> sum += result.groupValues[1].toInt() * result.groupValues[2].toInt()
            }
        }
        return sum

    }
}