package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.collections.cycle
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2


object Day23 : AdventSolution(2022, 23, "Unstable Diffusion") {

    override fun solvePartOne(input: String): Int {
        val elves = solve(input).elementAt(10)

        val width = elves.maxOf { it.x } - elves.minOf { it.x } + 1
        val height = elves.maxOf { it.y } - elves.minOf { it.y } + 1

        return width * height - elves.size
    }

    override fun solvePartTwo(input: String): Int =
        solve(input).zipWithNext().indexOfFirst { it.first == it.second } + 1


    private fun solve(input: String) = cycle().scan(parsePositions(input)) { oldPositions, directions ->
        val new = oldPositions.toMutableSet()

        oldPositions.groupBy { elf -> propose(elf, oldPositions, directions) }
            .asSequence()
            .filter { it.value.size == 1 }
            .forEach { (k, v) ->
                new.remove(v.first())
                new.add(k)
            }

        new
    }

}

private fun propose(elf: Vec2, positions: Set<Vec2>, directions: List<Direction>): Vec2 {

    val new = directions.mapNotNull {
        val test = when (it) {
            Direction.UP -> listOf(Vec2(-1, -1), Vec2(0, -1), Vec2(1, -1))
            Direction.RIGHT -> listOf(Vec2(1, -1), Vec2(1, 0), Vec2(1, 1))
            Direction.DOWN -> listOf(Vec2(-1, 1), Vec2(0, 1), Vec2(1, 1))
            Direction.LEFT -> listOf(Vec2(-1, -1), Vec2(-1, 0), Vec2(-1, 1))
        }

        (elf + it.vector).takeIf { test.none { elf + it in positions } }

    }

    return if (new.size in 1..3) new.first() else elf

}


private fun parsePositions(input: String) = input
    .lineSequence()
    .flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, ch ->
            Vec2(x, y).takeIf { ch == '#' }
        }
    }.toSet()

private fun cycle() =
    listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
        .cycle()
        .windowed(4)
