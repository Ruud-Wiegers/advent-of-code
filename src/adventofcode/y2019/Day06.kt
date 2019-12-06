package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day06.solve()

object Day06 : AdventSolution(2019, 6, "Universal Orbit Map") {

    override fun solvePartOne(input: String): Int {
        val satellites: Map<String, List<String>> = input
                .lineSequence()
                .map { it.split(')') }
                .groupBy({ it[0] }, { it[1] })

        fun check(obj: String, depth: Int): Int =
                depth + satellites[obj].orEmpty().sumBy { check(it, depth + 1) }

        return check("COM", 0)
    }

    override fun solvePartTwo(input: String): Int {
        val parents: Map<String, String> = input
                .lineSequence()
                .map { it.split(')') }
                .associate { it[1] to it[0] }

        val y = generateSequence("YOU", parents::get).toList().asReversed()
        val s = generateSequence("SAN", parents::get).toList().asReversed()

        val same = y.zip(s).count { (a, b) -> a == b }
        return y.size + s.size - 2 * same - 2
    }
}
