package adventofcode.y2016

import adventofcode.io.AdventSolution

object Day04 : AdventSolution(2016, 4, "Security Through Obscurity") {
    override fun solvePartOne(input: String) = parseRooms(input)
            .sumOf { it.id }
            .toString()

    override fun solvePartTwo(input: String) = parseRooms(input)
            .map { it.decrypt() to it.id }
            .first { "north" in it.first }
            .second
            .toString()


    private fun parseRooms(input: String): Sequence<Room> {
        return input.lineSequence()
                .map {
                    Room(
                            name = it.substringBeforeLast("-"),
                            id = it.substringAfterLast("-").substringBefore("[").toInt(),
                            checksum = it.substringAfterLast("[").dropLast(1)
                    )
                }
                .filter { it.isValid() }
    }

    data class Room(val name: String, val id: Int, val checksum: String) {
        fun isValid(): Boolean = name.groupingBy { it }
                .eachCount()
                .toSortedMap()
                .asSequence()
                .sortedByDescending { it.value }
                .map { it.key }
                .filterNot { it == '-' }
                .take(5)
                .joinToString("") == checksum

        fun decrypt(): String = name.map {
            when (it) {
                '-' -> '-'
                in 'a'..'z' -> ((((it - 'a') + id) % 26) + 'a'.code).toChar()
                else -> '?'
            }
        }
                .joinToString("")

    }
}