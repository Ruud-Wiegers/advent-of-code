package adventofcode.y2020

import adventofcode.io.AdventSolution
import java.math.BigInteger

fun main() = Day13.solve()

object Day13 : AdventSolution(2020, 13, "Shuttle Search")
{

    override fun solvePartOne(input: String): Int
    {
        val (ts, bussesStr) = input.lines()
        val t = ts.toInt()
        val busses = bussesStr.split(',').mapNotNull(String::toIntOrNull)

        fun f(x: Int) = x - t % x

        val b = busses.minByOrNull(::f)!!

        return b * f(b)
    }

    override fun solvePartTwo(input: String): Any
    {
        val (_, bussesStr) = input.lines()
        val busses = bussesStr.split(',').map { it.toIntOrNull() }.mapIndexedNotNull { i, v -> v?.let { i to it } }

        val congruences = busses.map { (i, b) -> b.toBigInteger() to ((b - i) % b).toBigInteger() }

        return crt(congruences)
    }

    private fun crt(congruences: List<Pair<BigInteger, BigInteger>>): BigInteger
    {
        val m = congruences.map { it.first }.reduce(BigInteger::times)
        return congruences
            .map { (mod, offset) -> offset * (m / mod) * (m / mod).modInverse(mod) }
            .reduce(BigInteger::plus) % m
    }
}
