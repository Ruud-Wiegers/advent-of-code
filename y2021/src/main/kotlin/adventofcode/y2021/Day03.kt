package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main()
{
    Day03.solve()
}

object Day03 : AdventSolution(2021, 3, "stuff!")
{
    override fun solvePartOne(input: String): Int = input
        .lines()
        .transpose()
        .map { it.count { it == '1' } >= it.length / 2 }
        .map { if (it) 1 else 0 }
        .fold(0) { acc, e -> acc * 2 + e }
        .let { it * ("111111111111".toInt(radix = 2) xor it) }

    private fun List<String>.transpose(): List<String> =
        this[0].indices.map { col -> map { it[col] } }.map { it.joinToString("") }

    override fun solvePartTwo(input: String): Int
    {
        val ogr = input.lines().toMutableList().findRating { ch, mostFrequent -> ch == mostFrequent }.toInt(radix = 2)
        val co2r = input.lines().toMutableList().findRating { ch, mostFrequent -> ch != mostFrequent }.toInt(radix = 2)

        return ogr * co2r
    }

    private inline fun MutableList<String>.findRating(crossinline matchCriteria: (current: Char, mostFrequent: Char) -> Boolean): String
    {
        first().indices.forEach { index ->
            val mfc = mostFrequentCharacterAt(index)
            retainAll { matchCriteria(mfc, it[index]) }
            if (size == 1) return single()
        }
        throw IllegalStateException()
    }

    private fun List<String>.mostFrequentCharacterAt(index: Int): Char =
        if (count { it[index] == '1' } >= size / 2) '1' else '0'
}
