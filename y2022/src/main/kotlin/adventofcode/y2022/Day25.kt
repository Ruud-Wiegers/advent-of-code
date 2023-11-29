package adventofcode.y2022

import adventofcode.io.AdventSolution


object Day25 : AdventSolution(2022, 25, "Full of Hot Air") {

    override fun solvePartOne(input: String) =
        input.lineSequence().map { fromBalancedQuinary(it) }.sum().toBalancedQuinary()


    private fun fromBalancedQuinary(it: String): Long = it.asSequence().fold(0L) { acc, digit ->
        val i = when (digit) {
            '=' -> -2
            '-' -> -1
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> throw NumberFormatException(it)
        }
        acc * 5 + i
    }

    private fun Long.toBalancedQuinary(): String {

        val quinary = this.toString(5)
        val x = quinary.reversed().map { Character.getNumericValue(it) }.scan((0 to 0)) { (_, carry), digit ->
            val n = digit + carry
            if (n > 2) n - 5 to 1 else n to 0

        }
        val withcarry = if (x.last().second == 1) x + (1 to 0) else x

        return withcarry.drop(1).reversed().map { it.first }.joinToString("") {
            when (it) {
                -2 -> "="
                -1 -> "-"
                else -> it.toString()
            }
        }
    }

    override fun solvePartTwo(input: String) = "Free Star!"


}

