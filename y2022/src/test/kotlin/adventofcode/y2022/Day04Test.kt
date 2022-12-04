package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day04Test {

    @Test
    fun solvePartOne() {
        assertEquals(2, Day04.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(4, Day04.solvePartTwo(testData))
    }

    private val testData = """2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8"""
}