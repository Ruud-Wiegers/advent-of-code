package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day07Test {

    @Test
    fun solvePartOne() {
        assertEquals(3749, Day07.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(11387, Day07.solvePartTwo(input))
    }

    val input = """190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20"""

}