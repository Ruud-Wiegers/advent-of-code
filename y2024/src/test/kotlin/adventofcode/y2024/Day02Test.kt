package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day02Test {

    @Test
    fun solvePartOne() {
        assertEquals(2, Day02.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(4, Day02.solvePartTwo(input))
    }

    private val input = """7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"""
}