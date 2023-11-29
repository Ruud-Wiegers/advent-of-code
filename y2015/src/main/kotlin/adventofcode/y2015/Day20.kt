package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day20 : AdventSolution(2015, 20, "Infinite Elves and Infinite Houses")
{

    override fun solvePartOne(input: String): Int
    {
        val sigma = input.toInt() / 10
        val arr = IntArray(sigma / 2) { 1 }
        for (elf in 2 until arr.size)
        {
            for (house in elf until arr.size step elf)
            {
                arr[house] += elf
            }
        }
        return arr.indexOfFirst { it > sigma }
    }

    override fun solvePartTwo(input: String): Int
    {
        val target = input.toInt() / 11
        val arr = IntArray(target / 2)
        for (visit in 1..50)
        {
            for (elf in 1 until arr.size / visit)
            {
                arr[elf * visit] += elf
            }
        }
        return arr.indexOfFirst { it > target }
    }
}



