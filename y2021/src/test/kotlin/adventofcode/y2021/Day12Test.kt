package adventofcode.y2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    @Test
    fun solvePartOne() {
        assertEquals(10, Day12.solvePartOne(small))
        assertEquals(19, Day12.solvePartOne(medium))
        assertEquals(226, Day12.solvePartOne(large))
    }

    @Test
    fun solvePartTwo() {
        assertEquals(36, Day12.solvePartTwo(small))
        assertEquals(103, Day12.solvePartTwo(medium))
        assertEquals(3509, Day12.solvePartTwo(large))
    }

    private val small = """start-A
start-b
A-c
A-b
b-d
A-end
b-end"""

    private val medium = """dc-end
HN-start
start-kj
dc-start
dc-HN
LN-dc
HN-end
kj-sa
kj-HN
kj-dc"""

    private val large = """fs-end
he-DX
fs-he
start-DX
pj-DX
end-zg
zg-sl
zg-pj
pj-he
RW-he
fs-DX
pj-RW
zg-RW
start-pj
he-WI
zg-he
pj-fs
start-RW"""
}