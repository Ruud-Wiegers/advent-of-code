package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day03.solve()
}
object Day03 : AdventSolution(2021, 3, "Binary Diagnostic")
{
    override fun solvePartOne(input: String): Int
    {
        val lines = input.lines()
        return lines[0].indices.map(lines::mostFrequentDigitAt)
            .joinToString("")
            .toInt(2)
            .let { it * (lines[0].replace('0','1').toInt(2) xor it) }
    }

    override fun solvePartTwo(input: String): Int
    {
        val generator = findRating(input) { d, mostFrequent -> d == mostFrequent }
        val scrubber = findRating(input) { d, mostFrequent -> d != mostFrequent }
        return generator * scrubber
    }
}

private inline fun findRating(input: String, crossinline matchCriteria: (d: Char, mostFrequent: Char) -> Boolean) =
    generateSequence(0, Int::inc)
        .scan(input.lines()) { candidates, index ->
            val mfd = candidates.mostFrequentDigitAt(index)
            candidates.filter { candidate -> matchCriteria(candidate[index], mfd) }
        }
        .firstNotNullOf { it.singleOrNull() }
        .toInt(radix = 2)

//verborgen shenanigans met het geval dat er evenveel nullen als enen zijn
private fun List<String>.mostFrequentDigitAt(index: Int): Char =
    (if (count { it[index] == '1' } >= size / 2) '1' else '0')
