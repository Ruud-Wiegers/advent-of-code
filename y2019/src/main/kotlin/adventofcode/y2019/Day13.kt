package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.language.intcode.IntCodeProgram

fun main() = Day13.solve()

object Day13 : AdventSolution(2019, 13, "Arcade") {

    override fun solvePartOne(input: String) = IntCodeProgram.fromData(input).run {
        execute()
        generateSequence { output() }.chunked(3).count { it[2] == 2L }
    }

    override fun solvePartTwo(input: String): Long {
        val arcadeProgram: IntCodeProgram = IntCodeProgram.fromData("2" + input.drop(1))

        var score = 0L
        val blocks = mutableSetOf<Pair<Long, Long>>()
        var ballX = 0L
        var paddleX = 0L

        while (true) {
            arcadeProgram.execute()
            generateSequence { arcadeProgram.output() }
                    .chunked(3)
                    .forEach { (x, y, value) ->
                        when {
                            x < 0       -> score = value
                            value == 0L -> blocks -= Pair(x, y)
                            value == 2L -> blocks += Pair(x, y)
                            value == 3L -> paddleX = x
                            value == 4L -> ballX = x
                        }
                    }
            if (blocks.isEmpty()) return score
            val joystickCommand = ballX.compareTo(paddleX).toLong()
            arcadeProgram.input(joystickCommand)
        }
    }

}
