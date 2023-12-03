package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day02.solve()
}

object Day02 : AdventSolution(2023, 2, "Cube Conundrum") {

    override fun solvePartOne(input: String) = parse(input)
        .filter { it.draws.all { (r, g, b) -> r <= 12 && g <= 13 && b <= 14 } }
        .sumOf { it.id }

    override fun solvePartTwo(input: String) = parse(input)
        .map { it.draws.reduce(::maxOf) }
        .sumOf { it.red * it.green * it.blue }
}


private fun parse(input: String): Sequence<Game> {
    return input.lineSequence().map { line ->
        val (idStr, drawsStr) = line.split(": ")
        val id = idStr.substringAfter("Game ").toInt()

        val draws = drawsStr.split("; ")
            .map { draw ->
                draw.split(", ")
                    .associate { singleDraw ->
                        singleDraw
                            .split(" ")
                            .let { (n, c) -> c to n.toInt() }
                    }
            }
            .map(::Draw)

        Game(id, draws)
    }
}


private data class Game(
    val id: Int,
    val draws: List<Draw>
)

private data class Draw(
    val red: Int,
    val green: Int,
    val blue: Int
) {
    constructor(map: Map<String, Int>) : this(
        red = map["red"] ?: 0,
        green = map["green"] ?: 0,
        blue = map["blue"] ?: 0
    )
}

private fun maxOf(a: Draw, b: Draw) = Draw(
    red = maxOf(a.red, b.red),
    green = maxOf(a.green, b.green),
    blue = maxOf(a.blue, b.blue)
)