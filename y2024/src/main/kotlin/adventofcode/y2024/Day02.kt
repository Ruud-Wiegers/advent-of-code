package adventofcode.y2024

import adventofcode.io.AdventSolution
import kotlin.math.abs

fun main() {
    Day02.solve()
}

object Day02 : AdventSolution(2024, 2, "Red-Nosed Reports") {

    override fun solvePartOne(input: String): Int =
        input.parseInput().count(::safe)

    override fun solvePartTwo(input: String): Int =
        input.parseInput().count(::dampeners)

    fun String.parseInput() = lines().map {
        it.split(" ").map(String::toInt)
    }

    private fun safe(list: List<Int>): Boolean {
        val pairs = list.zipWithNext()
        val monotone = pairs.all { (a, b) -> a > b } || pairs.all { (a, b) -> a < b }
        val close = pairs.all { (a, b) -> abs(a - b) in 1..3 }
        return monotone && close
    }

    private fun <T> List<T>.cut(i: Int) = take(i) + takeLast(lastIndex - i)

    private fun dampeners(list: List<Int>): Boolean = (0..list.lastIndex).any {
        safe(list.cut(it))
    }

}