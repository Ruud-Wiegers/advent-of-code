package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day01 : AdventSolution(2015, 1, "Not Quite Lisp") {

    override fun solvePartOne(input: String) = input.sumOf { if (it == '(') 1L else -1L }

    override fun solvePartTwo(input: String) = -input.foldIndexed(0) { index, acc, c ->
        when {
            acc < 0 -> acc
            acc == 0 && c == ')' -> -1 - index
            c == '(' -> acc + 1
            else -> acc - 1
        }
    }
}
