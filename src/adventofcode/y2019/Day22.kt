package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.SimpleParser
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

fun main() = Day22.solve()

object Day22 : AdventSolution(2019, 22, "Magic") {

    override fun solvePartOne(input: String) =
            shufflingRoutine(input, 10007)
                    .evaluate(2019.toBigInteger())

    override fun solvePartTwo(input: String) =
            shufflingRoutine(input, 119315717514047)
                    .pow(101741582076661)
                    .evaluateInverse(2020.toBigInteger())

    private fun shufflingRoutine(input: String, deckSize: Long) = parseInput(input, deckSize.toBigInteger())
            .fold(LinearEquation(ONE, ZERO, deckSize.toBigInteger())) { acc, exp -> exp.compose(acc) }

    private fun parseInput(input: String, deckSize: BigInteger): List<LinearEquation> {
        val p = SimpleParser<LinearEquation>()
                .apply {
                    rule("deal into new stack") { LinearEquation(deckSize - ONE, deckSize - ONE, deckSize) }
                    rule("cut (-?\\d+)") { (n) -> LinearEquation(ONE, deckSize - n.toBigInteger(), deckSize) }
                    rule("deal with increment (\\d+)") { (n) -> LinearEquation(n.toBigInteger(), ZERO, deckSize) }
                }
        return input.lines().mapNotNull { p.parse(it) }
    }

    private data class LinearEquation(val a: BigInteger, val b: BigInteger, val m: BigInteger) {
        fun evaluate(s: BigInteger) = (s * a + b) % m
        fun evaluateInverse(s: BigInteger) = (s - b).mod(m) * a.modInverse(m) % m

        fun compose(other: LinearEquation) = LinearEquation((a * other.a) % m, (a * other.b + b) % m, m)

        fun pow(exp: Long): LinearEquation {
            val binaryExpansion = generateSequence(exp) { it / 2 }.takeWhile { it > 0 }.map { it % 2 == 1L }
            val repeatedSquaring = generateSequence(this, { it.compose(it) })

            return binaryExpansion.zip(repeatedSquaring) { b, o -> o.takeIf { b } }
                    .filterNotNull()
                    .reduce(LinearEquation::compose)
        }
    }
}
