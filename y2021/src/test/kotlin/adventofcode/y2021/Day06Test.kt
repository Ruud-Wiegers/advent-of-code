package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day06Test {

    @Test
    fun solvePartOne() {
        assertEquals(5934, Day06.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(26984457539, Day06.solvePartTwo(testData))
    }

    private val testData = """3,4,3,1,2"""
}