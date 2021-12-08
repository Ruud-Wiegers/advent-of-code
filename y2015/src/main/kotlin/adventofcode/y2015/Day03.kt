package adventofcode.y2015

import adventofcode.AdventSolution

object Day03 : AdventSolution(2015, 3, "Perfectly Spherical Houses in a Vacuum") {

    override fun solvePartOne(input: String) = input.visitEach().size

    override fun solvePartTwo(input: String): Int {
        val route1 = input.filterIndexed { i, _ -> i % 2 == 0 }.visitEach()
        val route2 = input.filterIndexed { i, _ -> i % 2 != 0 }.visitEach()
        val visited = route1 + route2

        return visited.size +1
    }

    private fun String.visitEach() = asSequence()
            .scan(Pos(0, 0), Pos::next)
            .toSet()

    data class Pos(val x: Int, val y: Int) {
        fun next(ch: Char) = Pos(x + ch.dx(), y + ch.dy())

        private fun Char.dx() = when (this) {
            '>' -> 1
            '<' -> -1
            else -> 0
        }

        private fun Char.dy() = when (this) {
            '^' -> 1
            'v' -> -1
            else -> 0
        }
    }
}
