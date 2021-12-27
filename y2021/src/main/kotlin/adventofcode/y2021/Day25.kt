package adventofcode.y2021

import adventofcode.AdventSolution

object Day25 : AdventSolution(2021, 25, "Sea Cucumbers") {

    override fun solvePartOne(input: String) = generateSequence(input.lines(), ::step)
        .zipWithNext()
        .indexOfFirst { (a, b) -> a == b } + 1

    private fun step(field: List<String>): List<String> = field
        .map { ("${it.last()}$it${it[0]}").replace(">.", ".>").substring(1..it.length) }
        .transpose()
        .map { ("${it.last()}$it${it[0]}").replace("v.", ".v").substring(1..it.length) }
        .transpose()

    private fun List<String>.transpose(): List<String> =
        first().indices.map { index ->
            buildString {
                for (row in this@transpose) {
                    append(row[index])
                }
            }
        }


    override fun solvePartTwo(input: String) = "Free Star!"
}