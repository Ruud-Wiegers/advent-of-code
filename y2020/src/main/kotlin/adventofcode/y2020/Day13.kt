package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import java.math.BigInteger

fun main() = Day13.solve()

object Day13 : AdventSolution(2020, 13, "Shuttle Search")
{

    override fun solvePartOne(input: String): Int
    {
        val (ts, bussesStr) = input.lines()
        val t = ts.toInt()
        val busses = bussesStr.split(',').mapNotNull { it.toIntOrNull() }

        val b = busses.minByOrNull { it - (t % it) }!!

        return b * (b - (t % b))
    }

    override fun solvePartTwo(input: String): Any
    {
        val (ts, bussesStr) = input.lines()
        val t = ts.toInt()
        val busses = bussesStr.split(',').mapIndexedNotNull { i, s -> s.toIntOrNull()?.let { i to it } }

        val mods = busses.map { (i, b) -> b.toBigInteger() to ((b - i) % b).toBigInteger() }

        return crt(mods)
    }

    private fun crt(congruences: List<Pair<BigInteger, BigInteger>>): BigInteger
    {
        val m = congruences.map { it.first }.reduce(BigInteger::times)
        return congruences
            .map { (n, a) ->
                val ni = m / n
                a * ni.modInverse(n) * ni
            }
            .reduce(BigInteger::plus) % m
    }
}
