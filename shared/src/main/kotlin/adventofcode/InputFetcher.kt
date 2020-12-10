package adventofcode

import java.io.File
import java.io.InputStream
import java.net.URL

fun retrieveInput(day: Int, year: Int) =
        readFromCache(day, year) ?: readfromUrl(day, year).also { write(it, day, year) }

private fun readFromCache(day: Int, year: Int) =
        File("data/$year/input-$day.txt").takeIf { it.exists() }?.readText()

private fun readfromUrl(day: Int, year: Int) =
        URL("https://adventofcode.com/$year/day/$day/input")
                .openConnection()
                .apply { addRequestProperty("Cookie", "session=$AOC_SESSION") }
                .getInputStream()
                .use(InputStream::readBytes)
                .toString(Charsets.UTF_8)
                .trimEnd()

private fun write(input: String, day: Int, year: Int) {
    File("data/$year").mkdirs()
    File("data/$year/input-$day.txt").writeText(input)
}
