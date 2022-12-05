package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day05Test {

    @Test
    fun solvePartOne() {
        assertEquals("CMZ", Day05.solvePartOne(testData))
    }

    @Test
    fun solvePartTwo() {
        assertEquals("MCD", Day05.solvePartTwo(testData))
    }

    private val testData = """    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2"""
}