package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day07Test {

    @Test
    fun solvePartOne() {
        assertEquals(6440, Day07.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(5905, Day07.solvePartTwo(input))
    }

    private val input = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483 
        """.trimIndent()
}