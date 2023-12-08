package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day08Test {

    @Test
    fun solvePartOne() {
        val input = """
            RL
    
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """.trimIndent()

        assertEquals(2, Day08.solvePartOne(input))
    }

    @Test
    fun solvePartOne_Loop() {
        val input = """
            LLR
    
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """.trimIndent()

        assertEquals(6, Day08.solvePartOne(input))
    }

    // The solution for part two relies on some heavy input assumptions.
    // These are thankfully also true for the test case
    @Test
    fun solvePartTwo() {
        val input = """
            LR
    
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent()

        assertEquals(6, Day08.solvePartTwo(input))
    }
}