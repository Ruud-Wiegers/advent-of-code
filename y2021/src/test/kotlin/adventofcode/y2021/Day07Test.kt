package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day07Test {

    @Test
    fun solvePartOne() {
        assertEquals(37, Day07.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(168, Day07.solvePartTwo(testData))
    }

    private val testData = """16,1,2,0,4,2,7,1,2,14"""
}