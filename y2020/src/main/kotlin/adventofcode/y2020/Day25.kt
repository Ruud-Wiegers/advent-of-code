package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day25.solve()

object Day25 : AdventSolution(2020, 25, "Combo Breaker D:<")
{
    override fun solvePartOne(input: String): Any
    {
        val (k0, k1) = input.lines().map(String::toLong)
        val loop = generateSequence(1L) { it * 7 % 20201227L }.takeWhile { it != k0 }.count()
        return generateSequence(1L) { it * k1 % 20201227L }.drop(loop).first()
    }

    override fun solvePartTwo(input: String) = "Free Star!"
}