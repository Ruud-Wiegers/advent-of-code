package adventofcode.y2022

import adventofcode.io.AdventSolution
import adventofcode.util.transpose
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

object Day08 : AdventSolution(2022, 8, "Treetop Tree House") {

    override fun solvePartOne(input: String): Int {

        val heights = input.lines().map { it.map(Char::digitToInt) }

        val positions = heights.indices.flatMap { y -> heights[0].indices.map { x -> Vec2(x, y) } }

        fun line(position: Vec2, direction: Direction) =
            generateSequence(position) { it + direction.vector }
                .takeWhile { (x, y) -> x in heights.indices && y in heights[0].indices }
                .drop(1)

        return positions.count { tree ->
            Direction.values().any { d ->
                line(tree, d).all { (x, y) ->
                    heights[y][x] < heights[tree.y][tree.x]
                }
            }
        }

    }

    override fun solvePartTwo(input: String): Int {
        val heights = input.lines().map { it.map(Char::digitToInt) }
        val transposed = heights.transpose()

        val viewScores = heights.flatMapIndexed { y, line ->
            line.mapIndexed { x, tree ->
                val left = (heights[y].take(x).reversed())
                val right = (heights[y].drop(x + 1))
                val up = (transposed[x].take(y).reversed())
                val down = (transposed[x].drop(y + 1))

                listOf(left, right, up, down).map { row ->
                    val sight = row.takeWhile { it < tree }
                    if (sight.size == row.size)
                        sight.size
                    else
                        sight.size + 1
                }.reduce(Int::times)
            }
        }

        return viewScores.max()
    }


}