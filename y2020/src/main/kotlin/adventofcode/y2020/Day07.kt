package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() = Day07.solve()

object Day07 : AdventSolution(2020, 7, "Handy Haversacks")
{
    override fun solvePartOne(input: String): Int
    {
        val containedBy: Map<String, List<String>> =
            input.lineSequence()
                .map(::parseRule)
                .flatMap { (container, inside) -> inside.mapValues { container }.entries }
                .groupBy({ it.key }, { it.value })

        fun possibleContainers(bag: String): Set<String> =
            containedBy[bag].orEmpty()
                .map(::possibleContainers)
                .fold(setOf(bag), Set<String>::union)

        return possibleContainers("shiny gold").size - 1
    }

    override fun solvePartTwo(input: String): Long
    {
        val contents: Map<String, Map<String, Int>> =
            input.lineSequence()
                .associate(this::parseRule)

        fun countContents(bag: String): Long =
            contents[bag].orEmpty()
                .entries
                .sumOf { (color, count) -> count * countContents(color) } + 1

        return countContents("shiny gold") -1
    }

    private fun parseRule(line: String): Pair<String, Map<String, Int>>
    {
        val container = line.substringBefore(" bags contain")

        val containedBagCounts: Map<String, Int> = """(\d+) (\w+ \w+) bags?""".toRegex()
            .findAll(line)
            .map { it.destructured }
            .associate { (count, color) -> color to count.toInt() }

        return container to containedBagCounts
    }
}
