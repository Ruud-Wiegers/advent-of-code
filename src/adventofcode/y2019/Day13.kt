package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day13.solve()

object Day13 : AdventSolution(2019, 13, "Arcade") {

    override fun solvePartOne(input: String): Int {
        val arcade = parseProgram(input)
        arcade.execute()
       val b =  generateSequence {  arcade.output()}
                .chunked(3)
               .associate {  Pair(it[0],it[1]) to  it[2] }




return 0

    }
    override fun solvePartTwo(input: String):Long{
        val arcade = parseProgram("2"+input.drop(1))

        while (arcade.state != IntCodeProgram.State.Halted) {
            arcade.execute()
            val board = generateSequence {  arcade.output()}
                    .chunked(3)
                    .associate { Pair(it[0],it[1]) to it[2] }

            for (y in 0..23L){
                for (x in 0..36L){
                    print(board[x to y]?:'?')
                }
                println()
            }

            println(board.count { it.value == 2L })
            val ball = board.entries.first{it.value==4L}.key
            val paddle = board.entries.first{it.value==3L}.key

            arcade.input(ball.first.compareTo(paddle.first).toLong())
        }
        return 0
    }



    private fun parseProgram(data: String) = data
            .split(',')
            .map(String::toLong)
            .let{ IntCodeProgram(it) }

}
