package adventofcode.y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day18Test{
    @Test
    fun one() {
        assertEquals(64, Day18.solvePartOne(testdata))
    }

    @Test
    fun two() {
        assertEquals(58, Day18.solvePartTwo(testdata))
    }

    private val testdata="""2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5"""
}