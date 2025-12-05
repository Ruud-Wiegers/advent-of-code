package adventofcode.y2025

import adventofcode.io.AdventSolution

fun main() {
    Day03.solve()
}

object Day03 : AdventSolution(2025, 3, "Lobby") {

    override fun solvePartOne(input: String) = solve(input, 2)
    override fun solvePartTwo(input: String) = solve(input, 12)

    private fun solve(input: String, batteries: Int): Long = input.lines()
        .map { it.map(Char::digitToInt) }
        .sumOf { pickBattery(it, batteries, "").toLong() }

    private tailrec fun pickBattery(bank: List<Int>, batteriesLeft: Int, selected: String): String {
        if (batteriesLeft == 0) return selected
        val selectable = bank.dropLast(batteriesLeft - 1)
        val indexToSelect = selectable.indexOf(selectable.max())
        return pickBattery(bank.drop(indexToSelect + 1), batteriesLeft - 1, selected + selectable.max())
    }
}