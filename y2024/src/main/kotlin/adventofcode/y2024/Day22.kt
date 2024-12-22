package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day22.solve()
}

object Day22 : AdventSolution(2024, 22, "???") {

    override fun solvePartOne(input: String) = input.lines().sumOf {
        generateSequence(it.toLong()) { calculate(it) }.elementAt(2000)
    }

    override fun solvePartTwo(input: String): Int {
        val bananaPrices = input.lines().map {
            generateSequence(it.toLong()) { calculate(it) }.drop(1).take(2000)
                .map { (it % 10).toInt() }
                .toList()
        }

        val deltaPatterns = bananaPrices.map { it.zipWithNext { a, b -> b - a }.windowed(4) }

        val priceAtFirstOccurrence = deltaPatterns.zip(bananaPrices) { deltas, buyer ->
            deltas.zip(buyer.drop(4))
                .groupingBy { it.first }.aggregate { _, acc: Int?, (_, v), _ -> acc ?: v }
                .entries
        }

        return priceAtFirstOccurrence.flatten()
            .groupingBy { it.key }.fold(0) { acc, (_, v) -> acc + v }
            .values.max()
    }

}

private fun calculate(input: Long): Long {
    fun Long.step(f: (Long) -> Long) = (this xor f(this)) % 16777216
    return input.step { it * 64 }.step { it / 32 }.step { it * 2048 }
}
