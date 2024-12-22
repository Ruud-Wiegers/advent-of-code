package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day22Test {

    @Test
    fun solvePartOne() {
        assertEquals(37327623, Day22.solvePartOne("1\n10\n100\n2024"))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(23, Day22.solvePartTwo("1\n2\n3\n2024"))
    }
}