package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.collections.cycle
import adventofcode.util.math.lcm

fun main() {
    Day08.solve()
}

object Day08 : AdventSolution(2023, 8, "Haunted Wasteland") {

    override fun solvePartOne(input: String): Int {

        val directions = parse(input)

        val turnSequence = directions.turns.map { it == 'L' }.cycle()

        val positions = turnSequence.scan("AAA") { pos, turnLeft ->
            directions.map.getValue(pos).let { if (turnLeft) it.first else it.second }
        }

        return positions.indexOf("ZZZ")

    }

    override fun solvePartTwo(input: String): Long {
        val directions = parse(input)

        fun turnSequence() = directions.turns.map { it == 'L' }.cycle()


        val start = directions.map.keys.filter { it.endsWith("A") }.toSet()
        val target = directions.map.keys.filter { it.endsWith("Z") }.toSet()

        val paths = start.map { pos ->
            turnSequence().scan(pos) { p, turnLeft ->
                directions.map.getValue(p).let { if (turnLeft) it.first else it.second }
            }
        }

        val cycles = paths.map { it.indexOfFirst { it in target } }

        return cycles.map(Int::toLong).reduce(::lcm)


        /*
                Some testing of the behavior of the input

                val prefix = positions.map { it.indexOfFirst { it in target } }
                val cycle = buildSequence().zip(prefix) { pos, pre -> pos.drop(pre + 1).indexOfFirst { it in target } }
                prefix.let(::println)
                cycle.let(::println)
        */

        /*
           The input is very kind:
             - each path has only one exit,
             - cycles are a multiple of the turn instructions length
             - and the cycle length from that target is the same length as the initial walk from start.

            So no fiddliness, just find the point where all cycles line up, using LCM
         */


    }
}


private fun parse(input: String): Directions {
    val (turns, nodes) = input.split("\n\n")

    val map = nodes.lines().associate {
        val (node, left, right) = "[0-9A-Z]+".toRegex().findAll(it).map(MatchResult::value).toList()
        node to Pair(left, right)
    }

    return Directions(turns, map)
}


private data class Directions(val turns: String, val map: Map<String, Pair<String, String>>)