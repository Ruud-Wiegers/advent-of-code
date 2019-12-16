package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cycle
import kotlin.math.absoluteValue

fun main() = Day16.solve()

object Day16 : AdventSolution(2019, 16, "FFT") {

    override fun solvePartOne(input: String) =
            generateSequence(input.map { '0' - it }) { fft(it) }
                    .take(101)
                    .last()
                    .take(8)
                    .joinToString("")

    private fun fft(digits: List<Int>) = List(digits.size) {
        pattern(it)
                .take(digits.size)
                .zip(digits.asSequence(), Int::times)
                .sum()
                .let { (it % 10).absoluteValue }
    }

    private fun pattern(i: Int) = sequenceOf(0, 1, 0, -1)
            .flatMap { generateSequence { it }.take(i + 1) }
            .toList()
            .cycle()
            .drop(1)

    override fun solvePartTwo(input: String) = 0

}