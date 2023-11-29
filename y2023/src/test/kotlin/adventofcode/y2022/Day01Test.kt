package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day01Test {

    @Test
    fun solvePartOne() {
        assertEquals(24000, Day01.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(45000, Day01.solvePartTwo(testData))
    }

    private val testData = """1000
2000
3000

4000

5000
6000

7000
8000
9000

10000"""
}