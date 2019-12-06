package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day06.solve()

object Day06 : AdventSolution(2019, 6, "Universal Orbit Map") {

    override fun solvePartOne(input: String): Int {
        val orbits: Map<String, String> = parse(input)

        return orbits.keys.sumBy { satellite ->
            generateSequence(satellite, orbits::get).count() - 1
        }
    }

    override fun solvePartTwo(input: String): Int {
        val orbits: Map<String, String> = parse(input)

        val y = generateSequence("YOU", orbits::get).toList().asReversed()
        val s = generateSequence("SAN", orbits::get).toList().asReversed()

        val same = y.zip(s).count { (a, b) -> a == b }
        return y.size + s.size - 2 * same - 2
    }

    private fun parse(input: String) = input
            .lineSequence()
            .map { it.split(')') }
            .associate { it[1] to it[0] }
}
