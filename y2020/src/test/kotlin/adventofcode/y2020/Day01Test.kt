package adventofcode.y2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class Day01Test {
    @Test
    fun solvePartOne() {
        assertEquals(514579, Day01.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(241861950, Day01.solvePartTwo(input))
    }

}

private val input = """1721
979
366
299
675
1456"""
