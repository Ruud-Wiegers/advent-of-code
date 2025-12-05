package adventofcode.y2025

import adventofcode.io.AdventSolution
import java.util.stream.Collectors
import kotlin.streams.asSequence
import kotlin.streams.asStream
import kotlin.streams.toList

fun main() {
    Day02.solve()
}

object Day02 : AdventSolution(2025, 2, "Gift Shop") {

    override fun solvePartOne(input: String): Any {

        val ranges = input.split(",")
            .map {
                it.split("-").map(String::toLong).let { (a, b) -> a..b }
            }

        val ids = ranges.flatMap { it }

        fun isValid(id: String): Boolean {
            if (id.length % 2 != 0) return true
            val left = id.take(id.length / 2)
            val right = id.drop(id.length / 2)

            return left != right
        }

        return ids.filterNot { isValid(it.toString()) }.sum()
    }

    override fun solvePartTwo(input: String): Long {
        val ranges = input.splitToSequence(",")
            .map {
                it.split("-").map(String::toLong).let { (a, b) -> a..b }
            }


        fun isValid(id: String, cut: Int): Boolean {
            return id.take(cut).repeat(id.length / cut) != id
        }

        fun isValid(id: String) = (1..id.length / 2).all { isValid(id, it) }

        val ids = ranges.flatMap { it }

        return ids.filterNot { isValid(it.toString()) }.sum()
    }
}