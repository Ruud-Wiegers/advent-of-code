package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day05Test {

    @Test
    fun solvePartOne() {
        assertEquals(3, Day05.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(14, Day05.solvePartTwo(input))
    }

    private val input = """3-5
10-14
16-20
12-18

1
5
8
11
17
32"""
}