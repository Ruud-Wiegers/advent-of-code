package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() = Day06.solve()

object Day06 : AdventSolution(2020, 6, "Custom Customs")
{
    override fun solvePartOne(input: String) = solve(input, Set<Char>::union)

    override fun solvePartTwo(input: String) = solve(input, Set<Char>::intersect)

    private fun solve(input: String, reducer: Reducer) = input
        .splitToSequence("\n\n")
        .sumOf { g -> solveGroup(g, reducer) }

    private fun solveGroup(group: String, reducer: Reducer) = group
        .split("\n")
        .map(String::toSet)
        .reduce(reducer)
        .size
}

private typealias Reducer = (Set<Char>, Set<Char>) -> Set<Char>