package adventofcode.y2024

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day19Test {

    @Test
    fun solvePartOne() {
        Assertions.assertEquals(6, Day19.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(16, Day19.solvePartTwo(input))
    }

    val input = """r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb"""

}