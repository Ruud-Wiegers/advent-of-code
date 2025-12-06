package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day06Test {

    @Test
    fun solvePartOne() {
        assertEquals(4277556, Day06.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(3263827, Day06.solvePartTwo(input))
    }

    private val input = """123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   + """
}