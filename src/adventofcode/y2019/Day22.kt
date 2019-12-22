package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.SimpleParser
import java.math.BigInteger

fun main() = Day22.solve()

object Day22 : AdventSolution(2019, 22, "Magic") {

    override fun solvePartOne(input: String) =
            shufflingRoutine(input, 10007L)
                    .apply(2019.toBigInteger())

    override fun solvePartTwo(input: String) =
            shufflingRoutine(input, 119315717514047)
                    .pow(101741582076661)
                    .applyInverse(2020.toBigInteger())

    private fun shufflingRoutine(input: String, deckSize: Long) = parseInput(input)
            .map { it.asLinExp(deckSize) }
            .fold(LinExp(1, 0, deckSize)) { acc, exp -> exp.compose(acc) }

    private fun parseInput(input: String): List<Action> {
        val p = SimpleParser<Action>()
                .apply {
                    rule("deal into new stack") { Action.Reverse }
                    rule("cut (\\d+)") { (n) -> Action.Cut(n.toInt()) }
                    rule("cut -(\\d+)") { (n) -> Action.Cut(-n.toInt()) }
                    rule("deal with increment (\\d+)") { (n) -> Action.Increment(n.toInt()) }
                }

        return input.lines().mapNotNull { p.parse(it) }
    }

    private fun Action.asLinExp(deckSize: Long) = when (this) {
        Action.Reverse      -> LinExp(-1, -1, deckSize)
        is Action.Cut       -> LinExp(1, -n.toLong(), deckSize)
        is Action.Increment -> LinExp(n.toLong(), 0, deckSize)
    }

    data class LinExp(val a: BigInteger, val b: BigInteger, val m: BigInteger) {
        constructor(a: Long, b: Long, m: Long) : this(a.toBigInteger().mod(m.toBigInteger()), b.toBigInteger().mod(m.toBigInteger()), m.toBigInteger())

        fun compose(o: LinExp) = LinExp((a * o.a) % m, (a * o.b + b) % m, m)
        fun apply(s: BigInteger) = (s * a + b) % m
        fun applyInverse(s: BigInteger) = (s - b).mod(m) * a.modInverse(m) % m

        fun squared() = compose(this)

        fun pow(exp: Long): LinExp {
            val binaryExpansion = generateSequence(exp) { it / 2 }.takeWhile { it > 0 }.map { it % 2 == 1L }
            val repeatedSquaring = generateSequence(this, LinExp::squared)

            return binaryExpansion.zip(repeatedSquaring) { b, o -> o.takeIf { b } }
                    .filterNotNull()
                    .reduce(LinExp::compose)
        }
    }

    private sealed class Action {
        data class Cut(val n: Int) : Action()
        object Reverse : Action()
        data class Increment(val n: Int) : Action()
    }
}
