package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day03Test {

    @Test
    fun solvePartOne() {
        assertEquals(4361, Day03.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(467835, Day03.solvePartTwo(input))
    }

    private val input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..
            """.trimIndent()
}