package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day01.solve()

object Day01 : AdventSolution(2021, 1, "Report Repair")
{

    override fun solvePartOne(input: String): Int
    {

        val entries = input
            .lines()
            .map(String::toInt)
            .filter { it < 2020 }
            .toSortedSet()

        return entries
            .first { 2020 - it in entries }
            .let { (2020 - it) * it }
    }

    override fun solvePartTwo(input: String): Any
    {
        val entries = input.lines().map(String::toInt).filter { it < 2020 }.toSortedSet()

        for (a in entries.subSet(2020/3,2020))
        {
            for (b in entries.subSet((2020-a)/2,a+1))
            {
                val c = 2020 - a - b
                if (c in entries)
                {
                    return a * b * c
                }
            }
        }
        return "No solution"
    }
}
