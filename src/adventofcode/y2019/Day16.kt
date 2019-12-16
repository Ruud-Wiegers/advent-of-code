package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cycle
import adventofcode.util.collections.scan
import kotlin.math.absoluteValue

fun main() = Day16.solve()

object Day16 : AdventSolution(2019, 16, "Flawed Frequency Transmission") {

    override fun solvePartOne(input: String) =
            generateSequence(input.map { it - '0' }) { fft(it) }
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

    override fun solvePartTwo(input: String): String {

        val offset = input.take(7).toInt()

        val repsToSkip = offset / input.length

        val rr = repsToSkip until 10000

        var res = rr.flatMap { input.map { it - '0' } }.reversed()

        repeat(100) {
            res = res.asSequence().scan(0, Int::plus).map { it % 10 }.toList()
        }
        res = res.reversed()

        return res.drop(offset - (repsToSkip * input.length)).take(8).joinToString("")

    }

}
