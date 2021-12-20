package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day02Test {

    @Test
    fun solvePartOne() {
        assertEquals(150, Day02.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(900, Day02.solvePartTwo(testData))
    }

    private val testData = """forward 5
down 5
forward 8
up 3
down 8
forward 2"""
}