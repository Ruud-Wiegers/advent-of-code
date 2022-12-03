package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day02Test {

    @Test
    fun solvePartOne() {
        assertEquals(15, Day02.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(12, Day02.solvePartTwo(testData))
    }

    private val testData = """A Y
B X
C Z"""
}