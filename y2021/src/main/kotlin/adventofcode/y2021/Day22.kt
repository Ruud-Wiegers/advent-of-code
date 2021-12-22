package adventofcode.y2021

import adventofcode.AdventSolution

object Day22 : AdventSolution(2021, 22, "Reactor Reboot") {
    override fun solvePartOne(input: String) = parse(input).take(20).let(::solve)
    override fun solvePartTwo(input: String) = parse(input).let(::solve)

    private fun solve(input: List<Command>) = input
        .fold(listOf<Cube>()) { cubes, (turnOn, new) ->
            if (turnOn) addCube(cubes, new) else removeCube(cubes, new)
        }
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

    data class Command(val on: Boolean, val cube: Cube)
    data class Cube(val xs: IntRange, val ys: IntRange, val zs: IntRange) {

        val size = 1L * (xs.last - xs.first + 1) * (ys.last - ys.first + 1) * (zs.last - zs.first + 1)

        operator fun contains(o: Cube) =
            o.xs.first in xs && o.xs.last in xs
                    && o.ys.first in ys && o.ys.last in ys
                    && o.zs.first in zs && o.zs.last in zs

        private fun outside(o: Cube) =
            (xs.last < o.xs.first)
                    || (ys.last < o.ys.first)
                    || (zs.last < o.zs.first)
                    || (o.xs.last < xs.first)
                    || (o.ys.last < ys.first)
                    || (o.zs.last < zs.first)

        fun splitBy(o: Cube): Sequence<Cube> = splitXBy(o).flatMap { it.splitYBy(o) }.flatMap { it.splitZBy(o) }

        private fun splitXBy(o: Cube) = splitAxisBy(o, Cube::xs).map { copy(xs = it) }
        private fun splitYBy(o: Cube) = splitAxisBy(o, Cube::ys).map { copy(ys = it) }
        private fun splitZBy(o: Cube) = splitAxisBy(o, Cube::zs).map { copy(zs = it) }

        private inline fun splitAxisBy(o: Cube, dim: Cube.() -> IntRange): Sequence<IntRange> {

            val b0 = o.dim().first
            val b1 = o.dim().last
            val a0 = dim().first
            val a1 = dim().last
            return when {
                this.outside(o)    -> sequenceOf(dim())
                b0 > a0 && b1 < a1 -> sequenceOf(a0 until b0, o.dim(), b1 + 1..a1)
                b0 in a0 + 1..a1   -> sequenceOf(a0 until b0, b0..a1)
                b1 in a0 until a1  -> sequenceOf(a0..b1, b1 + 1..a1)
                else               -> sequenceOf(dim())
            }
        }
    }
}