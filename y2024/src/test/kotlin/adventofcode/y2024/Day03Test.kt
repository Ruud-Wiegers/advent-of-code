package adventofcode.y2024

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day03Test {

    @Test
    fun solvePartOne() {
        val input = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"
        assertEquals(161, Day03.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        val input = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"
        assertEquals(48, Day03.solvePartTwo(input))
    }

}