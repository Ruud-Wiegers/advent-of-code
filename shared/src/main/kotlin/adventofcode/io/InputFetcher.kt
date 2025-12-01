package adventofcode.io

import adventofcode.AOC_SESSION
import java.io.File
import java.io.InputStream
import java.net.URI

fun retrieveInput(day: Int, year: Int) =
    readFromCache(day, year) ?: readfromUrl(day, year).also { write(it, day, year) }

private fun readFromCache(day: Int, year: Int) =
    File("data/$year/input-$day.txt").takeIf { it.exists() }?.readText()

private fun readfromUrl(day: Int, year: Int) =
    URI("https://adventofcode.com/$year/day/$day/input")
        .toURL()
        .openConnection()
        .apply { addRequestProperty("Cookie", "session=$AOC_SESSION")
            addRequestProperty("User-Agent", "https://github.com/Ruud-Wiegers/advent-of-code by ruud.wiegers@gmail.com")
        }
        .getInputStream()
        .use(InputStream::readBytes)
        .toString(Charsets.UTF_8)
        .trimEnd()

private fun write(input: String, day: Int, year: Int) {
    File("data/$year").mkdirs()
    File("data/$year/input-$day.txt").writeText(input)
}
