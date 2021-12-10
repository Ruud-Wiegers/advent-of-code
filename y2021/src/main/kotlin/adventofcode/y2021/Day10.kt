package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day10.solve()
}

object Day10 : AdventSolution(2021, 10, "Syntax Scoring") {
    override fun solvePartOne(input: String) = input.lineSequence().sumOf { firstError(it) }

    private fun firstError(str: String): Int {
        val stack = mutableListOf<Char>()
        str.forEach {
            when (it) {
                ')' -> if (stack.removeLast() != '(') return 3
                ']' -> if (stack.removeLast() != '[') return 57
                '}' -> if (stack.removeLast() != '{') return 1197
                '>' -> if (stack.removeLast() != '<') return 25137
                else -> stack += it
            }
        }
        return 0
    }

    override fun solvePartTwo(input: String) = input.lineSequence()
        .filter { firstError(it) == 0 }
        .map(::remains)
        .map(::score)
        .toList()
        .sorted()
        .let { it[it.size / 2] }

    private fun remains(str: String) = buildList<Char> {
        str.forEach { if (it in ")]}>") removeLast() else add(it) }
    }

    private fun score(it: List<Char>) =
        it.reversed().fold(0L) { acc, c -> acc * 5 + "_([{<".indexOf(c) }
}
