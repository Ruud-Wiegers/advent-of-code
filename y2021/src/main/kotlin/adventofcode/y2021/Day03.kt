package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main()
{
    Day03.solve()
}

object Day03 : AdventSolution(2021, 3, "Binary Diagnostic")
{
    override fun solvePartOne(input: String): Int
    {
        val lines = input.lines()
        return lines[0].indices.map(lines::mostFrequentCharacterAt)
            .joinToString("")
            .toInt(2)
            .let { it * ("111111111111".toInt(2) xor it) }
    }

    override fun solvePartTwo(input: String): Int
    {
        val ogr = findRating(input) { ch, mostFrequent -> ch == mostFrequent }
        val co2r = findRating(input) { ch, mostFrequent -> ch != mostFrequent }
        return ogr * co2r
    }
}

private inline fun findRating(input:String,crossinline matchCriteria: (current: Char, mostFrequent: Char) -> Boolean): Int
{
    val candidates = input.lines().toMutableList()

    candidates[0].indices.forEach { index ->
        val mfc =candidates.mostFrequentCharacterAt(index)
        candidates.retainAll { matchCriteria(mfc, it[index]) }
        if (candidates.size == 1) return candidates.single().toInt(radix = 2)
    }
    throw IllegalStateException()
}

private fun List<String>.mostFrequentCharacterAt(index: Int): Char =
    if (count { it[index] == '1' } >= size / 2) '1' else '0'
