package adventofcode.y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day17Test{
    @Test
    fun one() {
        assertEquals(3068, Day17.solvePartOne(testdata))
    }

    @Test
    fun two() {
        assertEquals(1514285714288, Day17.solvePartTwo(testdata))
    }

    private val testdata=">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
}