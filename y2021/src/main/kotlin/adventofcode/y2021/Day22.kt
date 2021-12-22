package adventofcode.y2021

import adventofcode.AdventSolution

object Day22 : AdventSolution(2021, 22, "Reactor Reboot") {
    override fun solvePartOne(input: String) = parse(input).take(20).let(::solve)
    override fun solvePartTwo(input: String) = parse(input).let(::solve)

    private fun solve(input: List<Command>) = input
        .fold(listOf<Cube>()) { cubes, (turnOn, new) -> if (turnOn) addCube(cubes, new) else removeCube(cubes, new) }
        .sumOf(Cube::size)

    private fun addCube(cubes: List<Cube>, toAdd: Cube) = cubes + cubes.fold(listOf(toAdd)) { newFragments, existingCube ->
        newFragments.flatMap { fragment -> fragment.splitBy(existingCube) }.filterNot(existingCube::contains)
    }

    private fun removeCube(cubes: List<Cube>, toRemove: Cube) = cubes.flatMap { existingCube ->
        existingCube.splitBy(toRemove).filterNot(toRemove::contains)
    }

    private fun parse(input: String) = input.lines().map {
        """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()
            .matchEntire(it)!!.destructured
    }
        .map { (state, x0, x1, y0, y1, z0, z1) ->
            Command(
                state == "on",
                Cube(
                    x0.toInt()..x1.toInt(),
                    y0.toInt()..y1.toInt(),
                    z0.toInt()..z1.toInt()
                )
            )
        }
}

private data class Command(val on: Boolean, val cube: Cube)

private data class Cube(val xs: IntRange, val ys: IntRange, val zs: IntRange) {

    fun size() = xs.size() * ys.size() * zs.size()
    operator fun contains(o: Cube) = o.xs in xs && o.ys in ys && o.zs in zs
    private infix fun outside(o: Cube) = xs outside o.xs || ys outside o.ys || zs outside o.zs

    fun splitBy(o: Cube): Sequence<Cube> = splitXBy(o).flatMap { it.splitYBy(o) }.flatMap { it.splitZBy(o) }

    private fun splitXBy(o: Cube) = splitAxisBy(o, Cube::xs).map { copy(xs = it) }
    private fun splitYBy(o: Cube) = splitAxisBy(o, Cube::ys).map { copy(ys = it) }
    private fun splitZBy(o: Cube) = splitAxisBy(o, Cube::zs).map { copy(zs = it) }

    private inline fun splitAxisBy(o: Cube, selectAxis: Cube.() -> IntRange) =
        if (outside(o)) sequenceOf(selectAxis()) else selectAxis().splitBy(o.selectAxis())
}

private fun IntRange.size() = last - first + 1L
private operator fun IntRange.contains(o: IntRange) = o.first in this && o.last in this
private infix fun IntRange.outside(o: IntRange) = last < o.first || first > o.last

private fun IntRange.splitBy(o: IntRange): Sequence<IntRange> = sequenceOf(
    first,
    o.first.takeIf { it in first + 1..last },
    (o.last + 1).takeIf { it in first + 1..last },
    last + 1
)
    .filterNotNull()
    .windowed(2) { it[0] until it[1] }