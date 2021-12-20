package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day09Test {

    @Test
    fun solvePartOne() {
        assertEquals(15, Day09.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(1134, Day09.solvePartTwo(testData))
    }

    private val testData = """2199943210
3987894921
9856789892
8767896789
9899965678"""
}