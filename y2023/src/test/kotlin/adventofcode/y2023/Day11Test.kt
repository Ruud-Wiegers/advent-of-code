package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {

    @Test
    fun solvePartOne() {
        assertEquals(374, Day11.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(1030, Day11.solve(input, expansion = 10))
        assertEquals(8410, Day11.solve(input, expansion = 100))
    }

    private val input = """...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#....."""
}