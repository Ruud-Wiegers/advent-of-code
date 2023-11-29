package adventofcode.y2018

import adventofcode.io.AdventSolution

object Day11 : AdventSolution(2018, 11, "Chronal Charge") {

    override fun solvePartOne(input: String): String {
        val g = CumulativeGrid(300) { x, y -> power(x + 1, y + 1, input.toInt()) }

        var maxP = 0
        var answer = ""

        for (x in 1..300 - 3) {
            for (y in 1..300 - 3) {

                val p = g.cumulative(x - 1, y - 1, 3)
                if (p > maxP) {
                    maxP = p
                    answer = "${x + 1},${y + 1}"
                }
            }
        }
        return answer
    }

    override fun solvePartTwo(input: String): String {

        val g = CumulativeGrid(300) { x, y -> power(x + 1, y + 1, input.toInt()) }

        var maxP = 0
        var answer = ""

        for (sq in 1..300) {
            for (x in 1..300 - sq) {
                for (y in 1..300 - sq) {
                    val p = g.cumulative(x - 1, y - 1, sq)
                    if (p > maxP) {
                        maxP = p
                        answer = "${x + 1},${y + 1},$sq"
                    }
                }
            }
        }
        return answer
    }

    private fun power(x: Int, y: Int, serial: Int): Int {
        val id = x + 10
        val p = id * (id * y + serial)
        return p / 100 % 10 - 5
    }

    private class CumulativeGrid(size: Int, value: (x: Int, y: Int) -> Int) {
        private val g = List(size + 1) { IntArray(size + 1) }

        init {
            for (y in 1..size)
                for (x in 1..size)
                    g[y][x] = value(x, y) + g[y - 1][x] + g[y][x - 1] - g[y - 1][x - 1]
        }


        fun cumulative(x: Int, y: Int, sq: Int): Int =
                g[y + sq][x + sq] - g[y + sq][x] - g[y][x + sq] + g[y][x]
    }
}