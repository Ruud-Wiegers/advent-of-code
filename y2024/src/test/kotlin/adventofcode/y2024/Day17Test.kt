package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {

    @Test
    fun TestOne() {
        assertEquals("0,1,2", Day17.solvePartOne("""Register A: 10
Register B: 0
Register C: 0

Program: 5,0,5,1,5,4"""))
    }

    @Test
    fun solvePartOne() {
        assertEquals("4,6,3,5,6,3,5,2,1,0", Day17.solvePartOne(
            """Register A: 729
        Register B: 0
        Register C: 0
        
        Program: 0,1,5,4,3,0"""
        ))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(117440, Day17.solvePartTwo("""Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0"""))
    }

}