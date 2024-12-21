package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {


    @Test
    fun solvePartOne() {
       assertEquals(126384, Day21.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(154115708116294, Day21.solvePartTwo(input))
    }

    private val input = """029A
980A
179A
456A
379A"""

}