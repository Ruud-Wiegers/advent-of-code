package adventofcode.y2015

import adventofcode.AdventSolution


object Day16 : AdventSolution(2015, 16, "Aunt Sue") {

    override fun solvePartOne(input: String) = parseInput(input)
        .indexOfFirst { candidate ->
            candidate.all { (k, v) -> actualAunt[k] == v }
        } + 1

    override fun solvePartTwo(input: String) = parseInput(input)
        .indexOfFirst {
            it.all { (k, v) ->
                when (k) {
                    "cats", "trees" -> actualAunt.getValue(k) < v
                    "pomeranians", "goldfish" -> actualAunt.getValue(k) > v
                    else -> actualAunt[k] == v
                }
            }
        } + 1

    private fun parseInput(input: String) = input.lineSequence()
        .map {
            it.substringAfter(":")
                .split(',').associate { i -> parseItem(i) }
        }

    private fun parseItem(item: String) = item.substringBefore(':').trim() to
            item.substringAfter(":").trim().toInt()


    private val actualAunt = mapOf(
        "children" to 3,
        "cats" to 7,
        "samoyeds" to 2,
        "pomeranians" to 3,
        "akitas" to 0,
        "vizslas" to 0,
        "goldfish" to 5,
        "trees" to 3,
        "cars" to 2,
        "perfumes" to 1
    )

}