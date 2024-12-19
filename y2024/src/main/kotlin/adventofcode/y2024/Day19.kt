package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day19.solve()
}

object Day19 : AdventSolution(2024, 19, "Linen Layout") {

    override fun solvePartOne(input: String): Int {
        val (towels, designs) = parseInput(input)
        val regex = towels.joinToString(prefix = "(", separator = "|", postfix = ")*").toRegex()
        return designs.count(regex::matches)
    }

    override fun solvePartTwo(input: String): Long {
        val (towels, designs) = parseInput(input)

        val memo = mutableMapOf<String, Long>()
        fun matchCount(design: String): Long = memo.getOrPut(design) {
            if (design.isEmpty())
                1L
            else
                towels.filter(design::startsWith)
                    .map { design.drop(it.length) }
                    .sumOf(::matchCount)
        }

        return designs.sumOf(::matchCount)
    }

    private fun parseInput(input: String): Pair<List<String>, List<String>> {
        val (towelStr, designStr) = input.split("\n\n")
        val towels = towelStr.split(", ")
        val designs = designStr.lines()
        return Pair(towels, designs)
    }

}