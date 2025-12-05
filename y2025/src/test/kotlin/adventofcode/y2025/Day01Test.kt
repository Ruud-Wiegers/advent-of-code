package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day01Test {

    @Test
    fun solvePartOne() {
        assertEquals(3, Day01.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(6, Day01.solvePartTwo(input))
    }

    private val input = """L68
L30
R48
L5
R60
L55
L1
L99
R14
L82"""
}