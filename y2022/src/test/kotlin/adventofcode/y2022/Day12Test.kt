package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {

    @Test
    fun one() {
        assertEquals(31, Day12.solvePartOne(testdata))
    }

    @Test
    fun two() {
        assertEquals(29, Day12.solvePartTwo(testdata))
    }

    private val testdata = """Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi"""

}