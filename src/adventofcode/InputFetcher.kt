package adventofcode

import java.io.File
import java.net.URL


fun retrieveInput(day: Int, year: Int): String {
	val path = "aoc-$year-$day-input.txt"

	return readFromCache(path) ?: readfromUrl(day, year).also { write(it, path) }
}

private fun readFromCache(path: String): String? {
	val file = File(path)
	return if (file.exists()) {
		file.readText()
	} else null
}

private fun readfromUrl(day: Int, year: Int): String {
	return URL("https://adventofcode.com/$year/day/$day/input")
			.openConnection()
			.apply { addRequestProperty("Cookie", "session=$AOC_SESSION") }

			.getInputStream().use { it.readBytes() }
			.toString(Charsets.UTF_8)
			.trimEnd()
}

private fun write(input: String, path: String) {
	File(path).writeText(input)
}

