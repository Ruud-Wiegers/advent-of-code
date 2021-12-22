package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day22Compressed.solve()
}

object Day22Compressed : AdventSolution(2021, 22, "Reactor Reboot") {
    override fun solvePartOne(input: String) = parse(input).take(20).let(::solve)
    override fun solvePartTwo(input: String) = parse(input).let(::solve)

    private fun parse(input: String) = input.lines().map { line ->
        val turnsOn = line.startsWith("on")
        val numbers = "-?\\d+".toRegex().findAll(line).map { it.value.toInt() }
        val (xs, ys, zs) = numbers.chunked(2) { it[0]..it[1] }.toList()
        Command(turnsOn, Cuboid(xs, ys, zs))
    }

    private fun solve(commands: List<Command>): Long {

        val xCompressor = AxisCompressor(commands.map { it.cuboid }, Cuboid::xs)
        val yCompressor = AxisCompressor(commands.map { it.cuboid }, Cuboid::ys)
        val zCompressor = AxisCompressor(commands.map { it.cuboid }, Cuboid::zs)

        val compressedCommands = commands.map {
            it.copy(
                cuboid = Cuboid(
                    xs = xCompressor.compress(it.cuboid.xs),
                    ys = yCompressor.compress(it.cuboid.ys),
                    zs = zCompressor.compress(it.cuboid.zs)
                )
            )
        }

        val area = Array(zCompressor.sizes.size) {
            Array(yCompressor.sizes.size) {
                BooleanArray(xCompressor.sizes.size)
            }
        }

        for ((turnOn, c) in compressedCommands)
            for (x in c.xs)
                for (y in c.ys)
                    for (z in c.zs)
                        area[z][y][x] = turnOn

        var sum = 0L

        for (z in area.indices)
            for (y in area[0].indices)
                for (x in area[0][0].indices)
                    if (area[z][y][x]) sum += zCompressor.sizes[z] * yCompressor.sizes[y] * xCompressor.sizes[x]

        return sum
    }

    private data class Command(val on: Boolean, val cuboid: Cuboid)
    private data class Cuboid(val xs: IntRange, val ys: IntRange, val zs: IntRange)

    private class AxisCompressor(cuboids: List<Cuboid>, selectAxis: Cuboid.() -> IntRange) {

        private val ranges: List<Int> =
            cuboids.flatMap { listOf(it.selectAxis().first, it.selectAxis().last + 1) }.sorted()

        private val lookup = ranges.withIndex().associate { it.value to it.index }
        fun compress(uncompressed: IntRange) =
            lookup.getValue(uncompressed.first) until lookup.getValue(uncompressed.last + 1)

        val sizes: List<Long> = ranges.zipWithNext { a, b -> b - a }.map { it.toLong() }
    }
}