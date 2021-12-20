package adventofcode.y2021

import adventofcode.AdventSolution

object Day20 : AdventSolution(2021, 20, "Trench Map") {
    override fun solvePartOne(input: String) = solve(input, 2)
    override fun solvePartTwo(input: String) = solve(input, 50)

    private fun solve(input: String, rep: Int): Int {
        var (rules, grid) = parse(input)

        var void = 0
        repeat(rep) {
            val padded = padWith(grid, void)

            grid = nextStep(padded, rules)
            void = if (void == 1) rules.last() else rules[0]
        }
        return grid.sumOf { it.sum() }
    }

    private fun nextStep(padded: List<List<Int>>, rules: List<Int>): List<List<Int>> {

        val result = List(padded.size - 2) { y ->
            List(padded.size - 2) { x ->
                val list = padded[y].slice(x..x + 2) +
                        padded[y + 1].slice(x..x + 2) +
                        padded[y + 2].slice(x..x + 2)

                val f = list.fold(0) { acc, n -> acc * 2 + n }
                rules[f]
            }
        }
        return result
    }

    private fun padWith(grid: List<List<Int>>, n: Int): List<List<Int>> {
        val xPadded = grid.map { listOf(n, n) + it + listOf(n, n) }
        val row = xPadded[0].map { n }
        return listOf(row, row) + xPadded + listOf(row, row)
    }

    private fun parse(input: String): Pair<List<Int>, List<List<Int>>> {
        val (rules, grid) = input.split("\n\n")

        fun asInt(it: Char) = if (it == '#') 1 else 0

        return rules.map(::asInt) to grid.lines().map { it.map(::asInt) }
    }
}