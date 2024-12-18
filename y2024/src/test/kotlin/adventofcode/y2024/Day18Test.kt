package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day18Test {


    @Test
    fun solvePartOne() {
        assertEquals(
            22, Day18.solvePartOne(inputOne)
        )
    }

    @Test
    fun solvePartTwo() {
        assertEquals("6,1", Day18.solvePartTwo(inputTwo))
    }

    private val inputOne = "5,4\n4,2\n4,5\n3,0\n2,1\n6,3\n2,4\n1,5\n0,6\n3,3\n2,6\n5,1"
    private val inputTwo = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
        """.trimIndent()

}