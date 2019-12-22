package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.SimpleParser

fun main() = Day22.solve()


typealias DeckAction = (List<Int>) -> List<Int>

object Day22 : AdventSolution(2019, 22, "Magic") {

    override fun solvePartOne(input: String): Any? {

        val p = SimpleParser<DeckAction>()
                .apply {
                    rule("deal into new stack") { { it.reversed() } }
                    rule("deal with increment (\\d+)") { (n) ->
                        {
                            val res = IntArray(it.size)
                            it.forEachIndexed { i, v -> res[i * n.toInt() % it.size] = v }
                            res.toList()
                        }
                    }
                    rule("cut (\\d+)") { (n) -> { it.drop(n.toInt()) + it.take(n.toInt()) } }
                    rule("cut -(\\d+)") { (n) -> { it.takeLast(n.toInt()) + it.dropLast(n.toInt()) } }
                }


        return input.lines()
                .mapNotNull { p.parse(it) }
                .fold((0 until 10007).toList()) { deck, action -> action(deck) }
                .indexOf(2019)
    }

    override fun solvePartTwo(input: String): Any? {
        return "TODO"
    }
}
