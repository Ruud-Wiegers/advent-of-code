package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() {
    Day25.solve()
}

object Day25 : AdventSolution(2021, 25, "Sea Cucumbers") {

    override fun solvePartOne(input: String): Int {
        val seed = parse(input)
        yMax = seed.maxOf { it.key.y } + 1
        xMax = seed.maxOf { it.key.x } + 1
        return generateSequence(seed, ::step).zipWithNext().indexOfFirst { (a, b) -> a == b } + 1
    }

    private var yMax = 0
    private var xMax = 0
    private fun step(field: Map<Vec2, Boolean>): Map<Vec2, Boolean> {


        val f2 = field.mapKeys { (pos, goesRight) ->
            val newPos = if (goesRight) pos.copy(x = (pos.x + 1) % xMax) else pos
            if (field.containsKey(newPos)) pos else newPos
        }
        return f2.mapKeys { (pos, goesRight) ->
            val newPos = if (!goesRight) pos.copy(y = (pos.y + 1) % yMax) else pos
            if (f2.containsKey(newPos)) pos else newPos
        }
    }

    private fun print(field: Map<Vec2, Boolean>) {
        for (y in 0..9) {
            for (x in 0..9) {
                val c = field[Vec2(x, y)]
                print(
                    when (c) {
                        true -> '>'
                        false -> 'v'
                        null -> '.'
                    }
                )
            }
            println()
        }
        println()
    }


    private fun parse(input: String): Map<Vec2, Boolean> = buildMap {
        input.lineSequence().forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                when (ch) {
                    '>' -> put(Vec2(x, y), true)
                    'v' -> put(Vec2(x, y), false)
                }
            }
        }
    }

    override fun solvePartTwo(input: String) = "Free Star!"
}