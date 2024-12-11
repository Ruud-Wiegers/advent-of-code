package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() = Day11.solve()

object Day11 : AdventSolution(2024, 11, "Plutonian Pebbles") {

    override fun solvePartOne(input: String) = solve(input, 25)
    override fun solvePartTwo(input: String) = solve(input, 75)

    private fun solve(input: String, blinks: Int) =
        generateSequence(parseInput(input), ::blink).elementAt(blinks).values.sum()

    private fun parseInput(input: String): Map<Long, Long> {
        val parsed = input.split(" ").map(String::toLong)
        return parsed.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    }

    private fun blink(freq: Map<Long, Long>): Map<Long, Long> = freq.entries
        .flatMap { (value, count) -> changeStone(value).map { it to count } }
        .groupingBy { it.first }
        .fold(0L) { acc, (_, v) -> acc + v }

    private fun changeStone(value: Long): List<Long> = when {
        value == 0L -> listOf(1L)
        value.toString().length % 2 == 0 -> splitStone(value)
        else -> listOf(value * 2024L)
    }

    private fun splitStone(value: Long): List<Long> {
        val str = value.toString()
        val size = str.length / 2
        val l = str.take(size).toLong()
        val r = str.drop(size).toLong()
        return listOf(l, r)
    }
}