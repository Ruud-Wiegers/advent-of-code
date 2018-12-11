package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve

fun main(args: Array<String>) {
    Day11.solve()
}

object Day11 : AdventSolution(2018, 11, "The Stars Align") {

    override fun solvePartOne(input: String) = ""

    override fun solvePartTwo(input: String): String {

        val serial = input.toInt()

        //something something cumulative magic.
        val grid = (1..300).map { y ->
            (1..300).map { x -> power(x, y, serial) }.scan(0, Int::plus)
        }.scan(List(300) { 0 }) { a, b -> a.zip(b, Int::plus) }

        var maxP = 0
        var answer = ""

        for (sq in 1..300) {
            for (x in 1..301 - sq) {
                for (y in 1..301 - sq) {
                    val p = calculatePower(x, y, sq, grid)
                    if (p > maxP) {
                        maxP = p
                        answer = "$y,$x,$sq"
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

    private fun calculatePower(x: Int, y: Int, sq: Int, grid: List<List<Int>>): Int {

        fun get(x: Int, y: Int): Int = grid.getOrElse(y - 2) { return 0 }.getOrElse(x - 2) { 0 }
        return get(y + sq, x + sq) - get(y, x + sq) - get(y + sq, x) + get(y, x)
    }

}

//Analogous to the Rx scan operator. A fold that returns each intermediate value.
private fun <T, R> Iterable<T>.scan(initial: R, operation: (R, T) -> R): List<R> {
    var result: R = initial
    return this.map {
        result = operation(result, it)
        result
    }
}
