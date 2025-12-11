package adventofcode.y2025

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day11Test {

    @Test
    fun solvePartOne() {
        assertEquals(5, Day11.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(2, Day11.solvePartTwo(input2))
    }

    private val input = """aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out"""


    private val input2 = """svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out"""

}