package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {
    @Test
    fun solvePartOne() {
        assertEquals(46, Day16.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(51, Day16.solvePartTwo(input))
    }


    private val input = """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....""".trimIndent()
}