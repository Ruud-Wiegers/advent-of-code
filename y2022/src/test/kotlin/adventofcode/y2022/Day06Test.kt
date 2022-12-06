package adventofcode.y2022

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {

    @Test
    fun one() {
        fun test(expected: Int, input: String) = assertEquals(expected, Day06.solvePartOne(input))

        test(5, "bvwbjplbgvbhsrlpgdmjqwftvncz")
        test(6, "nppdvjthqldpwncqszvftbrmjlhg")
        test(10, "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
        test(11, "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
    }


    @Test
    fun two() {

        fun test(expected: Int, input: String) = assertEquals(expected, Day06.solvePartTwo(input))

        test(19, "mjqjpqmgbljsphdztnvjfqwrcgsmlb")
        test(23, "bvwbjplbgvbhsrlpgdmjqwftvncz")
        test(23, "nppdvjthqldpwncqszvftbrmjlhg")
        test(29, "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
        test(26, "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
    }
}

