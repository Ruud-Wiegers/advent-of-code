package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day04Test {

    @Test
    fun solvePartOne() {
        assertEquals(18, Day04.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(9, Day04.solvePartTwo(input))
    }

    val input = """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"""

}