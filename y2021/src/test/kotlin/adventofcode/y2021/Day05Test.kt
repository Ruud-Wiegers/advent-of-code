package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day05Test {

    @Test
    fun solvePartOne() {
        assertEquals(5, Day05.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(12, Day05.solvePartTwo(testData))
    }

    private val testData = """0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2"""
}