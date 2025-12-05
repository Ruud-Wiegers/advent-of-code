package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day02Test {

    @Test
    fun solvePartOne() {
        assertEquals(1227775554L, Day02.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(4174379265, Day02.solvePartTwo(input))
    }

    private val input = """11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124"""
}