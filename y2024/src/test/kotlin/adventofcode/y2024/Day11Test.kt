package adventofcode.y2024

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {

    @Test
    fun solvePartOne() {
        Assertions.assertEquals(55312, Day11.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(65601038650482, Day11.solvePartTwo(input))
    }

    val input = """125 17"""

}