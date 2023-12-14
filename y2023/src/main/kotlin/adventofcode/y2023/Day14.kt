package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.collections.firstDuplicate
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() {
    Day14.solve()
}

object Day14 : AdventSolution(2023, 14, "Parabolic Reflector Dish") {

    override fun solvePartOne(input: String): Int {

        val walls: Set<Vec2> = parseWalls(input)
        val boulders = parse(input, 'O')

        val movedNorth = move(boulders, walls, Direction.UP)


        val height = input.lines().size

        return movedNorth.sumOf { height - it.y }

    }

    override fun solvePartTwo(input: String): Int {

        val walls: Set<Vec2> = parseWalls(input)
        val boulders = parse(input, 'O')

        val cycleStart = generateSequence(boulders) { cycle(it, walls) }.firstDuplicate()
        val length = generateSequence(cycleStart) { cycle(it, walls) }.takeWhileDistinct().count()

        println(length)


        val leadup = generateSequence(boulders) { cycle(it, walls) }.indexOf(cycleStart)

        val remainder = (1_000_000_000 - leadup) % length

        val result = generateSequence(cycleStart) { cycle(it, walls) }.elementAt(remainder)




        val height = input.lines().size

        return result.sumOf { height - it.y }

    }

    private fun cycle(
        boulders: Set<Vec2>,
        walls: Set<Vec2>
    ): Set<Vec2> {
        val n = move(boulders, walls, Direction.UP)
        val w = move(n, walls, Direction.LEFT)
        val s = move(w, walls, Direction.DOWN)
        val e = move(s, walls, Direction.RIGHT)
        return e
    }

    private fun move(
        boulders: Set<Vec2>,
        walls: Set<Vec2>,
        direction: Direction,
    ): Set<Vec2> {
        val movedNorth = generateSequence(boulders) { rolling ->
            val (solid, movable) = rolling.partition { it + direction.vector in walls || it + direction.vector in rolling }


            if (movable.isEmpty()) null else solid.toSet() + movable.map { it + direction.vector }

        }
            .last()
        return movedNorth
    }

    private fun parseWalls(input: String): Set<Vec2> {
        val rocks = parse(input, '#')
        val width = input.lines()[0].length
        val height = input.lines().size

        val top = (0..width).map { x -> Vec2(x, -1) }
        val bottom = (0..width).map { x -> Vec2(x, height) }
        val left = (0..height).map { y -> Vec2(-1, y) }
        val right = (0..height).map { y -> Vec2(width, y) }
        val walls: Set<Vec2> = rocks + top + bottom + left + right
        return walls
    }

}


private fun parse(input: String, char: Char): Set<Vec2> = input.lines()
    .flatMapIndexed { y, line ->
        line.withIndex()
            .filter { it.value == char }
            .map { (x) -> Vec2(x, y) }
    }
    .toSet()
