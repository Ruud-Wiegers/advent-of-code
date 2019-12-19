package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() = Day19.solve()

object Day19 : AdventSolution(2019, 19, "Tractor Beam") {

    override fun solvePartOne(input: String): Int {
        (0 until 50L).forEach { y ->
            (0 until 50L).map { x -> isPulled(input, x, y) }.joinToString("") { if (it) "#" else "_"  }
                    .let(::println)
        }

        return (0 until 50L).sumBy { y ->
            (0 until 50L).count { x -> isPulled(input, x, y) }
        }
    }

    private fun isPulled(input: String, x: Long, y: Long): Boolean {
        val program = IntCodeProgram.fromData(input)
        program.input(x)
        program.input(y)
        program.execute()
        return program.output() == 1L
    }


    override fun solvePartTwo(input: String): Long? {

        tailrec fun Vec2.move(direction: Direction, into: Boolean): Vec2 =
                if (isPulled(input, this.x.toLong(), this.y.toLong()) == into)
                    this
                else (this + direction.vector).move(direction, into)

        val leftSequence = generateSequence(Vec2(15, 10)) {
            it.move(Direction.DOWN,false).move(Direction.RIGHT,true) }

        val rightSequence = generateSequence(Vec2(15, 10)) {
            it.move(Direction.DOWN,true).move(Direction.RIGHT,false)
        }

        leftSequence.take(10).forEach { println(it) }
        rightSequence.take(10).forEach { println(it) }

        return null
    }

}
