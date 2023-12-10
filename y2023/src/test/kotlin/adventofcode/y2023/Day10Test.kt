package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day10Test {

    @Test
    fun solvePartOne() {
        assertEquals(68, Day10.solvePartOne(input))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(10, Day10.solvePartTwo(input))
    }

    @Test
    fun solvePartTwo_test() {
        assertEquals(8, Day10.solvePartTwo(test))
    }

    @Test
    fun solvePartTwo_weird() {
        assertEquals(4, Day10.solvePartTwo(weird))
    }

    private val input = "FF7FSF7F7F7F7F7F---7\n" +
            "L|LJ||||||||||||F--J\n" +
            "FL-7LJLJ||||||LJL-77\n" +
            "F--JF--7||LJLJ7F7FJ-\n" +
            "L---JF-JLJ.||-FJLJJ7\n" +
            "|F|F-JF---7F7-L7L|7|\n" +
            "|FFJF7L7F-JF7|JL---7\n" +
            "7-L-JL7||F7|L7F-7F7|\n" +
            "L.L7LFJ|||||FJL7||LJ\n" +
            "L7JLJL-JLJLJL--JLJ.L"
}

private val test = """.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ..."""


private val weird = """..........
.S------7.
.|F----7|.
.||....||.
.||....||.
.|L-7F-J|.
.|..||..|.
.L--JL--J."""