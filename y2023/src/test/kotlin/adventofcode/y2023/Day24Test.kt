package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day24Test {


    private val input = """
            19, 13, 30 @ -2,  1, -2
            18, 19, 22 @ -1, -1, -2
            20, 25, 34 @ -2, -2, -4
            12, 31, 28 @ -1, -2, -1
            20, 19, 15 @  1, -5, -3""".trimIndent()

    @Test
    fun solvePartOne() {
        assertEquals(2, Day24.solve(input,7.toBigDecimal()..27.toBigDecimal()))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(47L, Day24.solvePartTwo(input))
    }


}