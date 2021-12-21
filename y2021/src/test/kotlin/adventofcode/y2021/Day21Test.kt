package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day21Test {

    @Test
    fun solvePartOne() {
        assertEquals(739785, Day21.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(444356092776315, Day21.solvePartTwo(testData))
    }

    private val testData = """4
     8
    """.trimMargin()
}
