package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day03Test {

    @Test
    fun solvePartOne() {
        assertEquals(357, Day03.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(3121910778619, Day03.solvePartTwo(input))
    }

    private val input = """987654321111111
811111111111119
234234234234278
818181911112111"""
}