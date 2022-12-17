package adventofcode.y2022

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day16Test{
    @Test
    fun one() {
        assertEquals(1651, Day16.solvePartOne(testdata))
    }

    @Test
    fun two() {
        assertEquals(1707, Day16.solvePartTwo(testdata))
    }

    private val testdata="""Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II"""
}