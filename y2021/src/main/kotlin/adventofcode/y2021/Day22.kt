package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec3

fun main() {
    Day22.solve()
}

object Day22 : AdventSolution(2021, 22, "Reactor Reboot") {
    override fun solvePartOne(input: String): Int {

        val area = mutableSetOf<Vec3>()

        fun Cube.on() {
            for (x in xs)
                for (y in ys)
                    for (z in zs)
                        area += Vec3(x, y, z)
        }

        fun Cube.off() {
            for (x in xs)
                for (y in ys)
                    for (z in zs)
                        area -= Vec3(x, y, z)

        }

        parse(input).take(20).forEach { (on, c) ->
            if (on) c.on() else c.off()
        }

        return area.size
    }


    override fun solvePartTwo(input: String): Long {
        val commands = parse(input).drop(20)
        val cubes = mutableListOf<Cube>()

        commands.forEach { (on, new) ->
            if (on) {
                val toAdd = cubes.fold(listOf(new)) { splitting, exist ->
                    println(cubes.size)
                    splitting.flatMap { it.splitBy(exist) }
                        .filter { new -> new !in exist }
                }
                cubes += toAdd
            }
        }

        return cubes.sumOf { it.size }
    }


    private fun parse(input: String) = input.lines().map {
        """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".toRegex()
            .matchEntire(it)!!.destructured
    }
        .map { (state, x0, x1, y0, y1, z0, z1) ->
            Command(
                state == "on",
                Cube(x0.toInt()..x1.toInt(), y0.toInt()..y1.toInt(), z0.toInt()..z1.toInt())
            )
        }

}

private data class Command(val on: Boolean, val cube: Cube)
private data class Cube(val xs: IntRange, val ys: IntRange, val zs: IntRange) {

    val size = 1L * (xs.last - xs.first +1) * (ys.last - ys.first+1) * (zs.last - zs.first+1)

    operator fun contains(o: Cube) =
        o.xs.first in xs && o.xs.last in xs
                && o.ys.first in ys && o.ys.last in ys
                && o.zs.first in zs && o.zs.last in zs

    fun splitBy(o: Cube): List<Cube> {
        if (xs.last < o.xs.first) return listOf(this)
        if (ys.last < o.zs.first) return listOf(this)
        if (zs.last < o.zs.first) return listOf(this)
        if (o.xs.last < xs.first) return listOf(this)
        if (o.ys.last < zs.first) return listOf(this)
        if (o.zs.last < zs.first) return listOf(this)
        return splitXBy(o).flatMap { it.splitYBy(o) }.flatMap { it.splitZBy(o) }
    }


    private fun splitXBy(o: Cube): List<Cube> = splitAxisBy(o, Cube::xs).map { copy(xs = it) }
    private fun splitYBy(o: Cube): List<Cube> = splitAxisBy(o, Cube::ys).map { copy(ys = it) }
    private fun splitZBy(o: Cube): List<Cube> = splitAxisBy(o, Cube::zs).map { copy(zs = it) }

    private inline fun splitAxisBy(o: Cube, dim: Cube.() -> IntRange): List<IntRange> {
        return when {
            o.dim().first > dim().first && o.dim().last < dim().last -> listOf(
                dim().first until dim().first,
                o.dim(),
                o.dim().last + 1..dim().last
            )
            o.dim().first in dim() -> listOf(dim().first until o.dim().first, o.dim(), o.dim().first..dim().last)
            o.dim().last in dim() -> listOf(dim().first..o.dim().last, o.dim().last + 1..dim().last)
            else -> listOf(dim())
        }
    }
}