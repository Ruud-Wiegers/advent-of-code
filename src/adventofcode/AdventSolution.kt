package adventofcode

import kotlin.system.measureTimeMillis

abstract class AdventSolution(val year: Int, val day: Int, val title: String) {

	init {
		require(year in 2015..2100) { "$year is not a valid year. AoC started in 2015" }
		require(day in 1..25) { "$day is not a valid day in december. Choose a day in 1-25" }
	}

	abstract fun solvePartOne(input: String): String
	abstract fun solvePartTwo(input: String): String
}

fun AdventSolution.solve() {
	val input = retrieveInput(day, year)

	println("Day $day: ${title.colored("32;1")}")

	var solution1 = ""
	var solution2 = ""
	val time1 = measureTimeMillis { solution1 = solvePartOne(input) }
	println("[${time1.toString().padStart(4).colored(gradeSolution(time1))} ms]  $solution1")
	val time2 = measureTimeMillis { solution2 = solvePartTwo(input) }
	println("[${time2.toString().padStart(4).colored(gradeSolution(time2))} ms]  $solution2")
	println()
	System.out.flush()
}


private fun String.colored(c: String) = "\u001B[${c}m$this\u001B[0m"

private fun gradeSolution(time: Long) = when {
	time < 250L -> "32"
	time < 500L -> "33"
	else -> "31"
}
