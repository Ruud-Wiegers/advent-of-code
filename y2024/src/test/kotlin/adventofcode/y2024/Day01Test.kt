package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day01Test {

    @Test
    fun solvePartOne() {
        assertEquals(11, Day01.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(31, Day01.solvePartTwo(input))
    }

    private val input = """3   4
4   3
2   5
1   3
3   9
3   3"""
}