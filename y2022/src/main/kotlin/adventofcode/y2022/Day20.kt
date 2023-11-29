package adventofcode.y2022

import adventofcode.io.AdventSolution

object Day20 : AdventSolution(2022, 20, "Grove Positioning System") {

    override fun solvePartOne(input: String): Long {

        val lines = parse(input).withIndex().toList()
        return solve(1, lines)
    }

    override fun solvePartTwo(input: String): Any? {
        val lines = parse(input).map { it * 811589153L }.withIndex().toList()
        return solve(10, lines)
    }

    private fun parse(input: String) = input.lines().map { it.toLong() }

    private fun solve(rounds: Int, lines: List<IndexedValue<Long>>): Long {
        val circle = ArrayDeque(lines)

        fun clamp(i: Long): Int = when {
            i < 0 -> i + circle.size
            i > circle.lastIndex -> i - circle.size
            else -> i
        }.toInt()

        repeat(rounds) {
            for (v in lines) {
                val pos = circle.indexOf(v)
                circle.remove(v)
                circle.add(clamp(pos + v.value % circle.size), v)
            }
        }

        val zero = circle.indexOfFirst { it.value == 0L }

        return listOf(1000L, 2000L, 3000L)
            .map { it % circle.size }
            .map(zero::plus)
            .map(::clamp)
            .map(circle::get)
            .sumOf { it.value }
    }
}
