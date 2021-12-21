package adventofcode.y2021

import adventofcode.AdventSolution

object Day20 : AdventSolution(2021, 20, "Trench Map") {
    override fun solvePartOne(input: String) = parse(input).let { (rules, grid) -> solve(rules, grid, 2) }
    override fun solvePartTwo(input: String) = parse(input).let { (rules, grid) -> solve(rules, grid, 50) }

    private fun solve(rules: List<Int>, grid: List<List<Int>>, steps: Int) =
        generateSequence(grid to 0) { (old, default) ->
            Pair(nextStep(old, rules, default), if (default == 1) rules.last() else rules[0])
        }.elementAt(steps).first.sumOf { it.sum() }

    private fun nextStep(grid: List<List<Int>>, rules: List<Int>, default: Int): List<List<Int>> {
        fun get(x: Int, y: Int) = grid.getOrNull(y)?.getOrNull(x) ?: default

        return List(grid.size + 2) { y ->
            List(grid.size + 2) { x ->
                var f = 0
                for (row in y - 2..y)
                    for (col in x - 2..x)
                        f = f * 2 + get(col, row)
                rules[f]
            }
        }
    }

    private fun parse(input: String): Pair<List<Int>, List<List<Int>>> {
        val (rules, grid) = input.split("\n\n")

        fun asInt(it: Char) = if (it == '#') 1 else 0

        return rules.map(::asInt) to grid.lines().map { it.map(::asInt) }
    }
}