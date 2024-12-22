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
            generateSequence(it.toLong()) { calculate(it) }.drop(1).take(2000).map { (it % 10).toInt() }.toList()
        }

        val deltaPatterns = bananaPrices.map { it.zipWithNext { a, b -> b - a }.windowed(4) }

        val priceAtFirstOccurrence = deltaPatterns.zip(bananaPrices) { deltas, buyer ->
            deltas.zip(buyer.drop(4))
                .groupBy({ it.first }, { it.second })
                .mapValues { it.value.first() }
                .entries
        }

        return priceAtFirstOccurrence
            .flatten()
            .groupBy({ it.key }, { it.value })
            .values
            .maxOf { it.sum() }
    }

}

private fun calculate(input: Long): Long {
    val one = (input xor input * 64) % 16777216
    val two = (one xor one / 32) % 16777216
    return (two xor two * 2048) % 16777216
}
