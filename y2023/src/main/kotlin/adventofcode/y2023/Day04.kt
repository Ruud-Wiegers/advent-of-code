package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day04.solve()
}

object Day04 : AdventSolution(2023, 4, "Scratch Cards") {

    override fun solvePartOne(input: String) = parse(input).sumOf(Scratchcard::score)

    override fun solvePartTwo(input: String): Int {
        val cards = parse(input).map(Scratchcard::wins).toList()

        val copies = IntArray(cards.size) { 1 }

        cards.forEachIndexed { i, wins ->
            for (next in i + 1..(i + wins).coerceAtMost(copies.lastIndex)) {
                copies[next] += copies[i]
            }
        }

        return copies.sum()
    }
}

private fun parse(input: String) = input.lineSequence().map { line ->
    val (idStr, own, win) = line.split(":", "|")

    Scratchcard(
        idStr.substringAfter("Card").trim().toInt(),
        own.split(" ").filter(String::isNotBlank).map(String::toInt),
        win.split(" ").filter(String::isNotBlank).map(String::toInt).toSet()
    )
}


data class Scratchcard(val id: Int, val own: List<Int>, val winning: Set<Int>) {
    fun wins() = own.count { it in winning }
    fun score() = wins().let { if (it == 0) 0 else 1 shl it - 1 }
}