package adventofcode.y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day23Test {
    @Test
    fun one() {
        assertEquals(110, Day23.solvePartOne(testdata))
    }

    @Test
    fun two() {
        assertEquals(20, Day23.solvePartTwo(testdata))
    }

    private val testdata = """....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#.."""
}