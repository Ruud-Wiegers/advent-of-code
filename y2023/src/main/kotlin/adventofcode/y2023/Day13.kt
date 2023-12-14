package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.algorithm.transposeString

fun main() {
    Day13.solve()
}

object Day13 : AdventSolution(2023, 13, "Point of Incidence") {

    override fun solvePartOne(input: String) = solve(input, String::equals)

    override fun solvePartTwo(input: String) = solve(input) { l, r ->
        l.zip(r).count { (a, b) -> a != b } == 1
    }

}

private fun solve(input: String, reflectionTest: (left: String, right: String) -> Boolean): Int {
    val patterns = parse(input)
    val cols = patterns.sumOf { indexOfMirror(it, reflectionTest) }
    val rows = patterns.sumOf { indexOfMirror(it.transposeString(), reflectionTest) }

    return 100 * cols + rows
}

private fun parse(input: String) = input.split("\n\n").map { it.lines() }

private fun indexOfMirror(lines: List<String>, reflectionTest: (left: String, right: String) -> Boolean) =
    generateSequence<Pair<List<String>, List<String>>>(Pair(emptyList(), lines)) { (left, right) ->
        Pair(left + right.first(), right.drop(1))
    }
        .drop(1).takeWhile { it.second.isNotEmpty() }
        .map { (l, r) -> Pair(l.asReversed().joinToString(""), r.joinToString("")) }
        .indexOfFirst { (l, r) -> reflectionTest(l.take(r.length), r.take(l.length)) } + 1
