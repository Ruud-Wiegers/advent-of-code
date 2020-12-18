package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day18.solve()

object Day18 : AdventSolution(2020, 18, "Operation Order")
{
    override fun solvePartOne(input: String) = input.lineSequence().sumOf { evaluate(it, ::evalLeftToRight) }
    override fun solvePartTwo(input: String) = input.lineSequence().sumOf { evaluate(it, ::evalWithWeirdPrecedence) }

    private inline fun evaluate(input: String, resolve: (Long, List<Pair<Char, Long>>) -> Long): Long
    {
        val scope = mutableListOf(mutableListOf<Any>())
        input.forEach { t ->
            when (t)
            {
                '('         -> scope.add(mutableListOf())
                ')'         -> scope.removeLast().let { scope.last() += eval(it, resolve) }
                in '0'..'9' -> scope.last() += t.toString().toLong()
                '+', '*'    -> scope.last() += t
            }
        }

        return eval(scope.single(), resolve)
    }

    private inline fun eval(tokens: List<Any>, resolve: (Long, List<Pair<Char, Long>>) -> Long): Long = tokens
        .drop(1)
        .chunked(2)
        .map { (operator, value) -> Pair(operator as Char, value as Long) }
        .let { resolve(tokens.first() as Long, it) }

    private fun evalLeftToRight(initial: Long, tokens: List<Pair<Char, Long>>): Long =
        tokens.fold(initial) { acc, (op, v) ->
            if (op == '+') acc + v else acc * v
        }

    private fun evalWithWeirdPrecedence(initial: Long, tokens: List<Pair<Char, Long>>): Long =
        tokens.fold(Pair(1L, initial)) { (prod, sum), (op, v) ->
            if (op == '+') Pair(prod, sum + v) else Pair(prod * sum, v)
        }.let { (prod, sum) -> prod * sum }
}