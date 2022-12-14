package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day14Test {

    @Test
    fun one() {
        assertEquals(24, Day14.solvePartOne(testdata))
    }

    @Test
    fun two() {
        assertEquals(93, Day14.solvePartTwo(testdata))
    }

    private val testdata = """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9"""

}