package adventofcode.y2021

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.transposeString

object Day25 : AdventSolution(2021, 25, "Sea Cucumbers") {

    override fun solvePartOne(input: String) = generateSequence(input.lines(), ::step)
        .zipWithNext()
        .indexOfFirst { (a, b) -> a == b } + 1

    private fun step(field: List<String>): List<String> = field
        .map { ("${it.last()}$it${it[0]}").replace(">.", ".>").substring(1..it.length) }
        .transposeString()
        .map { ("${it.last()}$it${it[0]}").replace("v.", ".v").substring(1..it.length) }
        .transposeString()

    override fun solvePartTwo(input: String) = "Free Star!"
}