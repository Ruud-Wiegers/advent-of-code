package adventofcode.y2015

import adventofcode.AdventSolution


object Day14 : AdventSolution(2015, 14, "Reindeer Olympics") {

	override fun solvePartOne(input: String) = parseInput(input)
		.map { it.raceFor(2503) }
		.maxOrNull()
		.toString()

	override fun solvePartTwo(input: String) = parseInput(input).let { r ->
		(1..2503).flatMap { s -> leadsAt(r, s) }
			.groupingBy { it }
			.eachCount()
			.values
			.maxOrNull()
			.toString()

	}
}


private fun parseInput(distances: String) = distances.lines()
		.mapNotNull { Regex("(\\w+) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.").matchEntire(it) }
		.map { it.destructured }
		.map { (name, speed, stamina, rest) ->
			RaceReindeer(name, speed.toInt(), stamina.toInt(), rest.toInt())
		}


data class RaceReindeer(private val name: String, private val speed: Int, private val stamina: Int, private val rest: Int) {
	fun raceFor(seconds: Int): Int {
		val fullCycles = seconds / (stamina + rest)
		val remainingTime = seconds % (stamina + rest)
		val racingSeconds = fullCycles * stamina + remainingTime.coerceAtMost(stamina)
		return racingSeconds * speed
	}
}

private fun leadsAt(reindeer: List<RaceReindeer>, seconds: Int): List<RaceReindeer> {
	val race = reindeer.map { it to it.raceFor(seconds) }

	val max = race.maxOf { it.second }

	return race.filter { it.second == max }.map { it.first }
}
