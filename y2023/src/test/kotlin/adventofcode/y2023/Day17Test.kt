package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day17Test {
    @Test
    fun solvePartOne() {
        assertEquals(102, Day17.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(94, Day17.solvePartTwo(input))
    }

    @Test
    fun solvePartTwo_cantStop() {
        val cantStop = """
            1111119
            9999919
            9999919
            9999919
            9999911""".trimIndent()

        assertEquals(42, Day17.solvePartTwo(cantStop))
    }

    @Test
    fun solvePartTwo_unstable() {
        val unstable = """
            111111111111
            999999999991
            999999999991
            999999999991
            999999999991""".trimIndent()

        assertEquals(71, Day17.solvePartTwo(unstable))
    }


    private val input = """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533""".trimIndent()
}






