package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.transpose
import adventofcode.util.collections.splitAfter
import adventofcode.util.collections.splitBefore
import adventofcode.util.collections.takeWhileDistinct

fun main() {
    Day14.solve()
}

object Day14 : AdventSolution(2023, 14, "Parabolic Reflector Dish") {

    override fun solvePartOne(input: String) = input.lines().map { it.toList() }.tiltNorth().let(::calculateLoad)

    override fun solvePartTwo(input: String): Int {

        val grid = input.lines().map { it.toList() }

        val beforeRepetition = generateSequence(grid, ::cycle).takeWhileDistinct().withIndex().last()
        val firstDuplicate = cycle(beforeRepetition.value)

        val leadup = generateSequence(grid, ::cycle).indexOf(firstDuplicate)
        val length = beforeRepetition.index - leadup + 1

        val remainder = (1_000_000_000 - leadup) % length

        val result = generateSequence(firstDuplicate, ::cycle).elementAt(remainder)

        return calculateLoad(result)
    }
}

private fun calculateLoad(result: List<List<Char>>): Int =
    result.asReversed().mapIndexed { y, row -> ( y+1) * row.count { it == 'O' } }.sum()

private fun cycle(grid: List<List<Char>>): List<List<Char>> = grid.tiltNorth().tiltWest().tiltSouth().tiltEast()

private fun List<List<Char>>.tiltNorth() = transpose().map(::moveRowWest).transpose()
private fun List<List<Char>>.tiltWest() = map(::moveRowWest)
private fun List<List<Char>>.tiltSouth() = transpose().map(::moveRowEast).transpose()
private fun List<List<Char>>.tiltEast() = map(::moveRowEast)

private fun moveRowWest(row: List<Char>) = row.splitAfter { it == '#' }.flatMap { it.sortedDescending() }
private fun moveRowEast(row: List<Char>) = row.splitBefore { it == '#' }.flatMap { it.sorted() }
