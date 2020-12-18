package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import java.util.Stack

fun main() = Day18.solve()

object Day18 : AdventSolution(2020, 18, "Operation Order")
{
    override fun solvePartOne(input: String) = input.lineSequence().sumOf { evaluate(it, ::evalLeftToRight) }
    override fun solvePartTwo(input: String) = input.lineSequence().sumOf { evaluate(it, ::evalWithWeirdPrecedence) }

    private fun evaluate(input: String, eval: (MutableList<Any>) -> Long): Long
    {
        val scope = Stack<MutableList<Any>>()
        scope.push(mutableListOf())
        input.forEach { t ->
            when (t)
            {
                '('         -> scope.push(mutableListOf())
                ')'         -> scope.pop().let { scope.peek() += eval(it) }
                in '0'..'9' -> scope.peek() += t.toString().toLong()
                '+', '*'    -> scope.peek() += t
            }
        }

        return eval(scope.pop())
    }

    private fun evalLeftToRight(tokens: MutableList<Any>): Long = tokens
        .drop(1)
        .chunked(2)
        .map { (o, v) -> o as Char to v as Long }
        .fold(tokens.first() as Long) { acc, (op, v) ->
            if (op == '+') acc + v else acc * v
        }

    private fun evalWithWeirdPrecedence(children: MutableList<Any>): Long
    {
        while (children.any { it == '+' })
        {
            val i = children.indexOfFirst { it == '+' } - 1
            val lhs = children.removeAt(i) as Long
            children.removeAt(i)
            val rhs = children.removeAt(i) as Long
            children.add(i, lhs + rhs)
        }

        return children.filterIsInstance<Long>().reduce(Long::times)
    }
}