package adventofcode.y2025

import adventofcode.io.AdventSolution
import kotlin.math.abs

fun main() {
    Day03.solve()
}

object Day03 : AdventSolution(2025, 3, "???") {

    override fun solvePartOne(input: String): Any {
        fun best(bank: String): Int {
            val pairIndexes = bank.indices.flatMap { left ->
                (left + 1..bank.lastIndex).map { right ->
                    bank[left] to bank[right]
                }
            }
            return pairIndexes.maxOf { (l, r) -> "$l$r".toInt() }
        }

        val banks = input.lines()

        return banks.sumOf { best(it) }
    }

    override fun solvePartTwo(input: String): Long {

        fun pickBattery(bank: List<Int>, batteriesLeft: Int, selected: String): String {
            if (batteriesLeft == 0) return selected

            val selectable = bank.dropLast(batteriesLeft - 1)

            val toSelect = selectable.max()
            val indexToSelect = selectable.indexOf(toSelect)

            return pickBattery(bank.drop(indexToSelect + 1), batteriesLeft - 1, selected + toSelect)


        }

        val banks = input.lines().map { it.map { it.digitToInt() } }

        return banks.sumOf { pickBattery(it, 12, "").toLong() }

    }
}