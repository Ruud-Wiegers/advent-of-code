package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {

    @Test
    fun solvePartOne() {
        assertEquals(3, Day25.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals("Free Star!", Day25.solvePartTwo(input))
    }

    private val input = """#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####"""
}