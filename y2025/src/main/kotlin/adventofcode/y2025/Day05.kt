package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreNeighbors
import kotlin.text.toLong

fun main() {
    Day05.solve()
}

object Day05 : AdventSolution(2025, 5, "Cafeteria") {

    override fun solvePartOne(input: String): Any {

        val (ranges, ingredients) = parse(input)

        return ingredients.count { i -> ranges.any { r -> i in r } }

    }

    override fun solvePartTwo(input: String): Long {
        val ranges = parse(input).ranges.sortedBy { it.first }


        var start = ranges.first().first
        var end = ranges.first().last

        val output = mutableListOf<LongRange>()

        ranges.forEach { new ->
            if (new.first <= end) {
                end = maxOf(end, new.last)
            } else {
                output += start..end
                start = new.first
                end = new.last
            }
        }

        output += start..end


        return output.sumOf { it.last - it.first + 1L }


    }
}

private fun parse(input: String): Stuff {
    val (rangesStr, ingredientsStr) = input.split("\n\n")

    val ranges = rangesStr.lines().map { it.split("-").let { (l, r) -> l.toLong()..r.toLong() } }

    val ingredients = ingredientsStr.lines().map { it.toLong() }

    return Stuff(ranges, ingredients)
}

private data class Stuff(val ranges: List<LongRange>, val ingredients: List<Long>)
