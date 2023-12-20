package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day20Test {

    @Test
    fun solvePartOne() {

        val input = """
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a""".trimIndent()

        assertEquals(32000000, Day20.solvePartOne(input))
    }

    @Test
    fun solvePartOne_b() {

        val input = """
        broadcaster -> a
        %a -> inv, con
        &inv -> b
        %b -> con
        &con -> output""".trimIndent()

        assertEquals(11687500, Day20.solvePartOne(input))    }
}