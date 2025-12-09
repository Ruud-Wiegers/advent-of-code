package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Test
    fun solvePartOne() {
        assertEquals(50, Day09.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(24, Day09.solvePartTwo(input))
    }

    private val input = """7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3"""
}