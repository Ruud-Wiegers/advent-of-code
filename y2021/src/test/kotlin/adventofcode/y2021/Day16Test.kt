package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day16Test {

    @Test
    fun solvePartOne() {
        assertEquals(16, Day16.solvePartOne("8A004A801A8002F478"))
        assertEquals(12, Day16.solvePartOne("620080001611562C8802118E34"))
        assertEquals(23, Day16.solvePartOne("C0015000016115A2E0802F182340"))
        assertEquals(31, Day16.solvePartOne("A0016C880162017C3686B18A3D4780"))

    }

    @Test
    fun solvePartTwo() {
        assertEquals(3, Day16.solvePartTwo("C200B40A82"))
        assertEquals(54, Day16.solvePartTwo("04005AC33890"))
        assertEquals(7, Day16.solvePartTwo("880086C3E88112"))
        assertEquals(9, Day16.solvePartTwo("CE00C43D881120"))
        assertEquals(1, Day16.solvePartTwo("D8005AC2A8F0"))
        assertEquals(0, Day16.solvePartTwo("F600BC2D8F"))
        assertEquals(0, Day16.solvePartTwo("9C005AC2F8F0"))
        assertEquals(1, Day16.solvePartTwo("9C0141080250320F1802104A08"))
    }
}