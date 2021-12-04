package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main()
{
    Day04.solve()
}

object Day04 : AdventSolution(2021, 4, "Giant Squid")
{
    override fun solvePartOne(input: String): Int
    {
        val (drawnNumbers, cards) = parseInput(input)

        return drawnNumbers.firstNotNullOf { n ->
            cards.forEach { it.crossOff(n) }
            cards.find(BingoCard::hasWon)?.let { it.sumUnmarked() * n }
        }
    }

    override fun solvePartTwo(input: String): Int
    {
        val (drawnNumbers, cards) = parseInput(input)

        drawnNumbers.forEach { n ->
            cards.forEach { it.crossOff(n) }
            cards.singleOrNull()?.takeIf(BingoCard::hasWon)?.let { return it.sumUnmarked() * n }
            cards.removeIf(BingoCard::hasWon)
        }
        throw IllegalStateException()
    }

    private fun parseInput(input: String): Pair<List<Int>, MutableList<BingoCard>>
    {
        val chunks = input.split("\n\n")

        val numbers = chunks[0].split(',').map(String::toInt)

        val bingoCards = chunks.drop(1)
            .map {
                it.lines().map { line ->
                    line.chunked(3).map(String::trim).map(String::toInt)
                }
            }
            .map(::BingoCard)
            .toMutableList()
        return Pair(numbers, bingoCards)
    }
}

private class BingoCard(
    val cells: List<Int>,
    val crosses: List<MutableList<Boolean>>
)
{
    constructor(cells: List<List<Int>>) : this(cells.flatten(), cells.map { MutableList(it.size) { false } })

    fun hasWon() = hasFullRow() || hasFullColumn()
    private fun hasFullRow() = crosses.any { row -> row.all { it } }
    private fun hasFullColumn() = crosses[0].indices.any { iCol -> crosses.all { row -> row[iCol] } }

    fun sumUnmarked() = cells.zip(crosses.flatten()) { num, crossed -> num.takeUnless { crossed } }.sumOf { it ?: 0 }

    fun crossOff(drawnNumber: Int)
    {
        val i = cells.indexOf(drawnNumber)
        if (i < 0) return
        crosses[i / 5][i % 5] = true
    }
}
