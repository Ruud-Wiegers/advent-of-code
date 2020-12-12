package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2
import kotlin.math.absoluteValue

fun main() = Day12.solve()

object Day12 : AdventSolution(2020, 12, "X X")
{

    override fun solvePartOne(input: String): Int
    {
        var orientation = 0
        var px = 0
        var py = 0

        fun process(instr: Char, v: Int)
        {
            when (instr)
            {
                'N' -> py += v
                'E' -> px += v
                'S' -> py -= v
                'W' -> px -= v
                'L' -> orientation = (orientation + 360 - v) % 360
                'R' -> orientation = (orientation + v) % 360
                'F' -> when (orientation)
                {
                    0    -> process('E', v)
                    90   -> process('S', v)
                    180  -> process('W', v)
                    270  -> process('N', v)
                    else -> throw IllegalStateException()
                }
            }
        }

        input
            .lines()
            .map { it[0] to it.drop(1).toInt() }
            .forEach { process(it.first, it.second) }

        return px.absoluteValue + py.absoluteValue
    }

    override fun solvePartTwo(input: String): Any
    {
        var ship = Vec2(0, 0)

        var waypoint = Vec2(10, 1)

        fun process(instr: Char, v: Int)
        {
            when (instr)
            {
                'N' -> waypoint += Vec2(0, v)
                'S' -> waypoint -= Vec2(0, v)
                'E' -> waypoint += Vec2(v, 0)
                'W' -> waypoint -= Vec2(v, 0)
                'L' -> repeat(v / 90) { waypoint = Vec2(-waypoint.y, waypoint.x) }
                'R' -> repeat(v / 90) { waypoint = Vec2(waypoint.y, -waypoint.x) }
                'F' -> ship += waypoint * v
            }
        }

        input
            .lines()
            .map { it[0] to it.drop(1).toInt() }
            .forEach { process(it.first, it.second) }

        return ship.x.absoluteValue + ship.y.absoluteValue
    }
}
