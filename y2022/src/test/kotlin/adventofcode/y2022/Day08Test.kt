package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {

    @Test
    fun one() {

assertEquals(21,        Day08.solvePartOne(testData))

    }

    @Test
    fun two() {

        assertEquals(8,        Day08.solvePartTwo(testData))

    }


    private val testData = """30373
25512
65332
33549
35390"""
}

