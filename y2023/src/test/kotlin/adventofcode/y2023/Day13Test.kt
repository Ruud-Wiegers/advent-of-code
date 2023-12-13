package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day13Test {

    @Test
    fun solvePartOne_a() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.""".trimIndent()
        assertEquals(5, Day13.solvePartOne(input))
    }

    @Test
    fun solvePartOne_b() {
        val input = """
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""".trimIndent()
        assertEquals(400, Day13.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
            
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#""".trimIndent()
        assertEquals(400, Day13.solvePartTwo(input))
    }
}



