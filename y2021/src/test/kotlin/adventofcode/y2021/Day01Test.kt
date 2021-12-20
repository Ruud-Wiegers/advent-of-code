package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day01Test {

    @Test
    fun solvePartOne() {
        assertEquals(7, Day01.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(5, Day01.solvePartTwo(testData))
    }

    private val testData = """199
200
208
210
200
207
240
269
260
263"""
}