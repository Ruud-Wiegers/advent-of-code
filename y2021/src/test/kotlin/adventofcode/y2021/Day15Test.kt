package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {

    @Test
    fun solvePartOne() {
        assertEquals(40, Day15.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(315, Day15.solvePartTwo(input))
    }

    private val input = """1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581"""
}