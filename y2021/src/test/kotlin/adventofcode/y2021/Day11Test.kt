package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {

    @Test
    fun solvePartOne() {
        assertEquals(1656, Day11.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(195, Day11.solvePartTwo(testData))
    }

    private val testData = """5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526"""
}