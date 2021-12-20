package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    @Test
    fun solvePartOne() {
        assertEquals(45, Day17.solvePartOne("target area: x=20..30, y=-10..-5"))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(112, Day17.solvePartTwo("target area: x=20..30, y=-10..-5"))
    }
}