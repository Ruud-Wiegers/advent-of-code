package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day10 : AdventSolution(2015, 10, "Elves Look, Elves Say")
{

    override fun solvePartOne(input: String) = generateSequence(input, this::looksay).elementAt(40).length

    override fun solvePartTwo(input: String) = generateSequence(input, this::looksay).elementAt(50).length

    private fun looksay(s: String): String
    {
        val out = StringBuilder()
        var count = 0
        var last = s[0]

        for (ch in s)
        {
            if (ch == last)
                count++
            else
            {
                out.append(count).append(last)
                count = 1
                last = ch
            }
        }

        return out.append(count).append(last).toString()
    }
}
