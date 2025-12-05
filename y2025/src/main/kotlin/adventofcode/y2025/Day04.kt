package adventofcode.y2025

import adventofcode.io.AdventSolution
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreNeighbors

fun main() {
    Day04.solve()
}

object Day04 : AdventSolution(2025, 4, "???") {

    override fun solvePartOne(input: String): Any {

        val stacks = buildSet {
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    if (ch == '@')
                        add(Vec2(x, y))

                }
            }
        }

        return stacks.count { it.mooreNeighbors().count { it in stacks } < 5 }
    }

    override fun solvePartTwo(input: String): Int {
        val stacks = buildSet {
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    if (ch == '@')
                        add(Vec2(x,y))

                }
            }
        }


        val last = generateSequence(stacks) { prev ->
            prev.filter { it.mooreNeighbors().count { it in prev } >=5}.toSet()
        }.takeWhileDistinct().last().size


        return stacks.size - last
    }
}