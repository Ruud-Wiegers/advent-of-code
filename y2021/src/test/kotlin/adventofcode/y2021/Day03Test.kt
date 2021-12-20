package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day03Test {

    @Test
    fun solvePartOne() {
        assertEquals(198, Day03.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(230, Day03.solvePartTwo(testData))
    }

    private val testData = """00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010"""
}