package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {


    @Test
    fun solvePartOne() {
        val grid = Day20.parseInput(input)
        val racetrack = Day20.path(grid)
        assertEquals(1, Day20.countValidCheats(racetrack, 2, 64))
        assertEquals(2, Day20.countValidCheats(racetrack, 2, 40))
        assertEquals(3, Day20.countValidCheats(racetrack, 2, 38))
        assertEquals(4, Day20.countValidCheats(racetrack, 2, 36))
        assertEquals(5, Day20.countValidCheats(racetrack, 2, 20))
        assertEquals(8, Day20.countValidCheats(racetrack, 2, 12))
    }

    @Test
    fun solvePartTwo() {
        val grid = Day20.parseInput(input)
        val racetrack = Day20.path(grid)
        assertEquals(3, Day20.countValidCheats(racetrack, 20, 76))
        assertEquals(7, Day20.countValidCheats(racetrack, 20, 74))
        assertEquals(29, Day20.countValidCheats(racetrack, 20, 72))
        assertEquals(41, Day20.countValidCheats(racetrack, 20, 70))
        assertEquals(55, Day20.countValidCheats(racetrack, 20, 68))
        assertEquals(67, Day20.countValidCheats(racetrack, 20, 66))
    }

    private val input = """###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############"""

}