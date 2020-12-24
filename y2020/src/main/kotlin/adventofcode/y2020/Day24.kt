package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() = Day24.solve()

object Day24 : AdventSolution(2020, 24, "Lobby Layout")
{
    override fun solvePartOne(input: String) = blackTiles(input).size

    override fun solvePartTwo(input: String) = generateSequence(Conway(blackTiles(input)), Conway::next)
        .take(101).last().activeCells.size

    private fun blackTiles(input: String): Set<Vec2> = input.lines()
        .map(::parse)
        .map(this::toCoordinate)
        .groupingBy { it }
        .eachCount()
        .filterValues { it % 2 == 1 }
        .keys

    private fun parse(input: String): List<Direction> = buildList {
        val iter = input.iterator()

        while (iter.hasNext())
        {
            val new = when (iter.nextChar())
            {
                'e'  -> Direction.E
                'w'  -> Direction.W
                'n'  -> if (iter.nextChar() == 'e') Direction.NE else Direction.NW
                's'  -> if (iter.nextChar() == 'e') Direction.SE else Direction.SW
                else -> throw IllegalStateException()
            }
            add(new)
        }
    }

    private fun toCoordinate(input: List<Direction>) = input.fold(Vec2.origin, Vec2::step)
}

private fun Vec2.step(d: Direction) = when (d)
{
    Direction.NE -> Vec2(x + 1, y + 1)
    Direction.E  -> Vec2(x + 1, y)
    Direction.SE -> Vec2(x, y - 1)
    Direction.SW -> Vec2(x - 1, y - 1)
    Direction.W  -> Vec2(x - 1, y)
    Direction.NW -> Vec2(x, y + 1)
}

private enum class Direction
{ NE, E, SE, SW, W, NW }

private data class Conway(val activeCells: Set<Vec2>)
{
    fun next(): Conway = activeCells
        .flatMapTo(mutableSetOf(), this::neighborhood)
        .let {
            it.removeIf { !aliveInNext(it) }
            Conway(it)
        }

    private fun aliveInNext(c: Vec2): Boolean = neighborhood(c).count { it in activeCells } in if (c in activeCells) 2..3 else 2..2

    private fun neighborhood(c: Vec2) = Direction.values().map(c::step) + c
}