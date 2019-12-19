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

        fun beamSizeAt(a: Vec2) = walk(startOfBeam(a) + Vec2(1, -1), Vec2(1, -1), false).count()

        fun approximation(start: Vec2, step: Int) = generateSequence(start) { it + Vec2(step, step * 2) }
                .takeWhile { beamSizeAt(it) < 99 }.last()

        val approx = generateSequence(256) { it / 2 }
                .takeWhile { it > 0 }
                .fold(Vec2(0, 5)) { acc, step -> approximation(acc, step) }

        return generateSequence(approx) { it + Direction.DOWN.vector }
                .map { startOfBeam(it) }
                .dropWhile { beamSizeAt(it) < 100 }
                .first()
                .let { 10000 * (it.x + 1) + it.y - 100 }
    }

}
