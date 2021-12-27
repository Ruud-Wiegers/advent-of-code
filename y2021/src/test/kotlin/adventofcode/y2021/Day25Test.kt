package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day25Test {

    @Test
    fun solvePartOne() {
        assertEquals(58, Day25.solvePartOne(testData))
    }

    private val testData = """
v...>>.vv>
.vv>>.vv..
>>.>v>...v
>>v>>.>.v.
v>v.vv.v..
>.>>..v...
.vv..>.>v.
v.v..>>v.v
....v..v.>
    """.trimMargin()
}
