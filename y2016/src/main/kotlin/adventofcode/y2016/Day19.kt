package adventofcode.y2016

import adventofcode.io.AdventSolution
import kotlin.math.log2

object Day19 : AdventSolution(2016, 19, "An Elephant Named Joseph") {

    //Jospehus problem
    override fun solvePartOne(input: String): String {
        val solved = 2 * (input.toInt() - (1 shl log2(input.toDouble()).toInt())) + 1
        return solved.toString()
    }

    //more complicated pattern, brute force instead
    override fun solvePartTwo(input: String): String {
        val elves = (1..input.toInt()).toList()
        return playElvesGame(elves, ::filterDayTwo).toString()
    }

    private fun playElvesGame(elves: List<Int>, filter: (List<Int>) -> List<Int>): Int =
        if (elves.size == 1)
            elves[0]
        else
            playElvesGame(filter(elves), filter)

    private fun filterDayTwo(list: List<Int>): List<Int> = list
        .filterIndexed { index, _ -> index < list.size / 2 || index % 3 == 2 - (list.size % 3) }
        .let { it.rotate(list.size - it.size) }


    private fun <T> List<T>.rotate(cut: Int): List<T> = drop(cut) + take(cut)
}
