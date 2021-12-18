package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cartesian

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2021, 18, "Snailfish") {
    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map(this::toSnailNumber)
        .reduce(SnailNumber::plus)
        .magnitude()

    override fun solvePartTwo(input: String) = input
        .lines()
        .map(this::toSnailNumber)
        .cartesian()
        .filter { it.first != it.second }
        .map { (a, b) -> a + b }
        .maxOf(SnailNumber::magnitude)

    private fun toSnailNumber(s: String) = SnailNumber(buildList {
        var depth = 0
        s.forEach { ch ->
            when (ch) {
                '[' -> depth++
                ']' -> depth--
                in '0'..'9' -> add(Pair(ch - '0', depth))
            }
        }
    })

    private data class SnailNumber(val values: List<Pair<Int, Int>>) {
        operator fun plus(o: SnailNumber) = SnailNumber((values + o.values).map { (a, b) -> a to b + 1 }).reduce()

        fun reduce(): SnailNumber = explode()?.reduce() ?: split()?.reduce() ?: this

        private fun split(): SnailNumber? {
            val i = values.indexOfFirst { it.first > 9 }
            return if (i < 0) null
            else SnailNumber(values.toMutableList().apply {
                set(i, Pair(values[i].first / 2, values[i].second + 1))
                add(i + 1, Pair((values[i].first + 1) / 2, values[i].second + 1))
            })
        }

        private fun explode(): SnailNumber? {
            val i = values.indexOfFirst { it.second == 5 }
            return if (i < 0) null else SnailNumber(values.toMutableList().apply {
                if (i > 0) set(i - 1, values[i - 1].let { (v0, d0) -> v0 + get(i).first to d0 })
                if (i + 1 < values.lastIndex) set(i + 2, values[i + 2].let { (v0, d0) -> v0 + get(i + 1).first to d0 })
                set(i, 0 to 4)
                removeAt(i + 1)
            })
        }

        fun magnitude(): Int {
            val stack = mutableListOf<Pair<Int, Int>>()
            values.forEach {
                stack.add(it)
                while (stack.size > 1 && stack.last().second == stack[stack.lastIndex - 1].second) {
                    val (v2, d2) = stack.removeLast()
                    val (v1, _) = stack.removeLast()
                    stack.add(3 * v2 + 2 * v1 to d2 - 1)
                }
            }
            return stack.first().first
        }
    }
}
