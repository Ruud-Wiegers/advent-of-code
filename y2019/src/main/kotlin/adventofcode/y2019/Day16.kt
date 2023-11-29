package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import kotlin.math.absoluteValue

fun main() = Day16.solve()

object Day16 : AdventSolution(2019, 16, "Flawed Frequency Transmission") {

    override fun solvePartOne(input: String) =
            generateSequence(input.map(Character::getNumericValue), this::fft)
                    .drop(100)
                    .first()
                    .take(8)
                    .joinToString("")

    private fun fft(digits: List<Int>) = List(digits.size) { index ->
        val period = 4 * (index + 1)

        fun sum(start: Int) = (start..digits.lastIndex step period).sumOf { blockStart ->
            val blockEnd = (blockStart + index).coerceAtMost(digits.lastIndex)
            (blockStart..blockEnd).sumOf { digits[it] }
        }

        (sum(index) - sum(3 * index + 2)).absoluteValue % 10
    }

    override fun solvePartTwo(input: String): String {
        val offset = input.take(7).toInt()
        val reverseOffset = 10000 * input.length - offset - 8

        val relevantInput = input.repeat(10000 - offset / input.length).reversed().map { it - '0' }
        val transformed = generateSequence(relevantInput) {
            it.scan(0) { a, b -> (a + b) % 10 }
        }.drop(100).first()

        return transformed.drop(reverseOffset).take(8).joinToString("").reversed()
    }
}
