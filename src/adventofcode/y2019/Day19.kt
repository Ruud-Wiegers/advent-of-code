package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() = Day19.solve()

object Day19 : AdventSolution(2019, 19, "Tractor Beam") {

    override fun solvePartOne(input: String): Int {
        return 201
    }


    override fun solvePartTwo(input: String): Int? {
        val baseprogram = IntCodeProgram.fromData(input)

        fun isPartOfBeam(p: Vec2) =
                baseprogram.copy(data = baseprogram.data.copy(memory = baseprogram.data.memory.toMutableMap())).run {
                    input(p.x.toLong())
                    input(p.y.toLong())
                    execute()
                    output() == 1L
                }

        fun walk(p: Vec2, delta: Vec2, ontoBeam: Boolean) =
                generateSequence(p) { it + delta }
                        .takeWhile { isPartOfBeam(it) != ontoBeam }


        fun startOfBeam(a: Vec2) = walk(a, Vec2(1, -1), true).lastOrNull() ?: Vec2.origin

        fun beamSizeAtLeast(a: Vec2, target: Int) = isPartOfBeam(startOfBeam(a) + Vec2(target, -target))

        fun approximation(start: Vec2, step: Int) = generateSequence(startOfBeam(start)) { startOfBeam(it + Vec2(0, step)) }
                .takeWhile { !beamSizeAtLeast(it, 99) }
                .last()

        val approx = generateSequence(1024) { it / 2 }
                .takeWhile { it > 0 }
                .fold(Vec2(0, 5)) { acc, step -> approximation(acc, step) }

        return generateSequence(startOfBeam(approx)) { it + Direction.DOWN.vector }
                .map { startOfBeam(it) }
                .first { beamSizeAtLeast(it, 100) }
                .let { 10000 * (it.x + 1) + it.y - 100 }
    }

}
