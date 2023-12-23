package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.util.SimpleParser
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

fun main() = Day22.solve()

object Day22 : AdventSolution(2019, 22, "Slam Shuffle") {

    override fun solvePartOne(input: String) = parseInput(input)
            .composeShufflingRoutine(10007.toBigInteger())
            .evaluate(2019.toBigInteger())

    override fun solvePartTwo(input: String) = parseInput(input)
            .composeShufflingRoutine(119315717514047.toBigInteger())
            .repeat(101741582076661)
            .evaluateInverse(2020.toBigInteger())


    private val parser = SimpleParser<ShufflingStep>().apply {
        rule("deal into new stack") { ShufflingStep.NewStack }
        rule("cut (-?\\d+)") { (n) -> ShufflingStep.Cut(n.toBigInteger()) }
        rule("deal with increment (\\d+)") { (n) -> ShufflingStep.Increment(n.toBigInteger()) }
    }

    private fun parseInput(input: String): Sequence<ShufflingStep> =
            input.lineSequence().mapNotNull { parser.parse(it) }

    private fun Sequence<ShufflingStep>.composeShufflingRoutine(deckSize: BigInteger): LinearEquation = this
            .map { it.toLinearEquation(deckSize) }
            .reduce { acc, exp -> exp.compose(acc) }

    private fun ShufflingStep.toLinearEquation(deckSize: BigInteger) = when (this) {
        ShufflingStep.NewStack     -> LinearEquation(deckSize - ONE, deckSize - ONE, deckSize)
        is ShufflingStep.Cut       -> LinearEquation(ONE, deckSize - n, deckSize)
        is ShufflingStep.Increment -> LinearEquation(n, ZERO, deckSize)
    }


    private sealed class ShufflingStep {
        object NewStack : ShufflingStep()
        class Cut(val n: BigInteger) : ShufflingStep()
        class Increment(val n: BigInteger) : ShufflingStep()
    }

    private data class LinearEquation(val a: BigInteger, val b: BigInteger, val m: BigInteger) {
        fun evaluate(s: BigInteger) = (s * a + b) % m
        fun evaluateInverse(s: BigInteger) = (s - b).mod(m) * a.modInverse(m) % m

        fun compose(other: LinearEquation) = LinearEquation((a * other.a) % m, (a * other.b + b) % m, m)

        fun repeat(exp: Long): LinearEquation {
            val repeatedSquaring = generateSequence(this) { it.compose(it) }
            val binaryExpansion = generateSequence(exp) { it / 2 }.takeWhile { it > 0 }.map { it % 2 == 1L }

            return repeatedSquaring.zip(binaryExpansion) { partial, occurs -> partial.takeIf { occurs } }
                    .filterNotNull()
                    .reduce(LinearEquation::compose)
        }
    }
}
