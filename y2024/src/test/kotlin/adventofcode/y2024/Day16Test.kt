package adventofcode.y2024

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {

    @Test
    fun solvePartOne() {
        Assertions.assertEquals(7036, Day16.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(45, Day16.solvePartTwo(input))
    }

    val input = """###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############"""

}