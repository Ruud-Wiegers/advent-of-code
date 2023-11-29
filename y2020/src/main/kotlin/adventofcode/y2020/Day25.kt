package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() = Day25.solve()

object Day25 : AdventSolution(2020, 25, "Combo Breaker D:<") {
    override fun solvePartOne(input: String): Long {

        val (k0, k1) = input.lines().map(String::toLong)

        fun seq(sn: Long) = generateSequence(1L) { it * sn % 20201227L }

        val loopSize = seq(7).indexOf(k0)

        return seq(k1).elementAt(loopSize)
    }


    override fun solvePartTwo(input: String) = "Free Star!"
}
