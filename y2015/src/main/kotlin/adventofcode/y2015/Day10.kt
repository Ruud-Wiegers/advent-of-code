package adventofcode.y2015

import adventofcode.AdventSolution

object Day10 : AdventSolution(2015, 10, "Elves Look, Elves Say") {

    override fun solvePartOne(input: String) = generateSequence(input, this::looksay).drop(40).first().length

    override fun solvePartTwo(input: String) = generateSequence(input, this::looksay).drop(50).first().length

    private fun looksay(s: String): String {
        val out = StringBuilder()
        var count = 1
        var last = s[0]

        for (i in 1 until s.length) {
            if (s[i] == last)
                count++
            else {
                out.append("$count$last")
                count = 1
                last = s[i]
            }
        }
        return out.append("$count$last").toString()
    }
}
