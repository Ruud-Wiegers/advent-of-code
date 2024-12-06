package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day06Test {

    @Test
    fun solvePartOne() {
        assertEquals(41, Day06.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(6, Day06.solvePartTwo(input))
    }

    val input = """....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#..."""

}