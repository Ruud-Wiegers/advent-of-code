package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Test
    fun solvePartOne() {
        assertEquals(1928, Day09.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(2858, Day09.solvePartTwo(input))
    }

    val input = "2333133121414131402"

}