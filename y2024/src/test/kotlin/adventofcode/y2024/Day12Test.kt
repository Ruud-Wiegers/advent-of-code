package adventofcode.y2024

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    @Test
    fun solvePartOne() {
        Assertions.assertEquals(1930, Day12.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(1206, Day12.solvePartTwo(input))
    }

    val input = """RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE"""

}