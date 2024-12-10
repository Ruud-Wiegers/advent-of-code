package adventofcode.y2024

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    @Test
    fun solvePartOne() {
        Assertions.assertEquals(36, Day10.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(81, Day10.solvePartTwo(input))
    }

    val input = """89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732"""

}