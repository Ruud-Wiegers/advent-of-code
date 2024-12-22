package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    @Test
    fun solvePartOne() {
        assertEquals(37327623, Day22.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(23, Day22.solvePartTwo(input))
    }

    private val input = """1
10
100
2024"""
}