package adventofcode.y2015

import adventofcode.AdventSolution
import java.math.BigInteger

object Day24 : AdventSolution(2015, 24, "It Hangs in the Balance")
{

    override fun solvePartOne(input: String): String
    {
        val presents = list(input).asSequence()
        val target = presents.sum() / 3
        return subsetSum(presents, target)
    }

    override fun solvePartTwo(input: String): String
    {
        val presents = list(input).asSequence()
        val target = presents.sum() / 4
        return subsetSum(presents, target)
    }

    private fun subsetSum(presents: Sequence<Int>, target: Int): String
    {
        return generateSequence(presents.map { listOf(it) }) { prev ->
            prev.flatMap { subset ->
                val t = target - subset.sum()
                presents.dropWhile { it <= subset.last() }
                    .takeWhile { it <= t }
                    .map { subset + it }
            }
        }
            .map { s -> s.filter { it.sum() == target } }
            .first { it.any() }
            .map { it.fold(BigInteger.ONE) { x, y -> x * y.toBigInteger() } }
            .minOrNull()
            .toString()
    }

    private fun list(input: String): List<Int> = input.lines().map { it.toInt() }
}
