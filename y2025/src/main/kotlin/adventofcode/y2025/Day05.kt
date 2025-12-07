package adventofcode.y2025

import adventofcode.io.AdventSolution

fun main() = Day05.solve()


object Day05 : AdventSolution(2025, 5, "Cafeteria") {

    override fun solvePartOne(input: String): Any {

        val (ranges, ingredients) = parse(input)

        return ingredients.count { i -> ranges.any { r -> i in r } }
    }

    override fun solvePartTwo(input: String): Long {

        val ranges = parse(input).first.sortedBy { it.first }

        val output = buildList {
            var currentRange = ranges.first()

            ranges.forEach { new ->
                if (new.first > currentRange.last) {
                    add(currentRange)
                    currentRange = new
                } else if (currentRange.last < new.last) {
                    currentRange = currentRange.first..new.last
                }
            }

            add(currentRange)
        }

        return output.sumOf { it.last - it.first + 1L }
    }
}

private fun parse(input: String): Pair<List<LongRange>, List<Long>> {
    val (rangesStr, ingredientsStr) = input.split("\n\n")

    val ranges = rangesStr.lines().map { it.split("-").let { (l, r) -> l.toLong()..r.toLong() } }

    val ingredients = ingredientsStr.lines().map { it.toLong() }

    return Pair(ranges, ingredients)
}
