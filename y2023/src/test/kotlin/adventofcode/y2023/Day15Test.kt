package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day15Test {

    @Test
    fun solvePartOne() {
        val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7".trimIndent()
        assertEquals(1320, Day15.solvePartOne(input))
    }


    @Test
    fun solvePartTwo() {
        val input = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7".trimIndent()
        assertEquals(145, Day15.solvePartTwo(input))
    }
}



