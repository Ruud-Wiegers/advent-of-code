package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day14Test {

    @Test
    fun solvePartOne() {
        assertEquals(1588, Day14.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(2188189693529, Day14.solvePartTwo(input))
    }

    private val input = """NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C"""
}