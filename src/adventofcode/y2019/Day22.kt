package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.SimpleParser

fun main() = Day22.solve()

object Day22 : AdventSolution(2019, 22, "Magic") {

    override fun solvePartOne(input: String) = parseInput(input)
            .fold((0 until 10007).toList()) { deck, action ->
                when (action) {
                    is Action.Cut        -> deck.drop(action.n) + deck.take(action.n)
                    is Action.InverseCut -> deck.takeLast(action.n) + deck.dropLast(action.n)
                    Action.Reverse       -> deck.reversed()
                    is Action.Increment  -> IntArray(deck.size).apply {
                        deck.forEachIndexed { i, v -> this[i * action.n % deck.size] = v }
                    }.toList()
                }
            }
            .indexOf(2019)


    override fun solvePartTwo(input: String): Any? {
        return parseInput(input)
    }

    private fun parseInput(input: String): List<Action> {
        val p = SimpleParser<Action>()
                .apply {
                    rule("deal into new stack") { Action.Reverse }
                    rule("deal with increment (\\d+)") { (n) -> Action.Increment(n.toInt()) }
                    rule("cut (\\d+)") { (n) -> Action.Cut(n.toInt()) }
                    rule("cut -(\\d+)") { (n) -> Action.InverseCut(n.toInt()) }
                }

        return input.lines().mapNotNull { p.parse(it) }
    }
}

private sealed class Action {
    data class Cut(val n: Int) : Action()
    data class InverseCut(val n: Int) : Action()
    object Reverse : Action()
    data class Increment(val n: Int) : Action()
}