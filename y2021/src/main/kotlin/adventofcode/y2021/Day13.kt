package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2
import kotlin.math.abs

object Day13 : AdventSolution(2021, 13, "Passage Pathing") {
    override fun solvePartOne(input: String): Int {
        val (points, folds) = parse(input)
        return foldPaper(points, folds.take(1)).size
    }

    override fun solvePartTwo(input: String): String {
        val (points, folds) = parse(input)
        val result = foldPaper(points, folds)
        return prettyString(result)
    }

    private fun parse(input: String): Pair<List<Vec2>, List<Pair<String, Int>>> {
        val (a, b) = input.split("\n\n")
        val points = a.lineSequence().map {
            val (x, y) = it.split(',').map(String::toInt)
            Vec2(x, y)
        }.toList()

        val regex = """fold along ([xy])=(\d+)""".toRegex()
        val folds = b.lineSequence()
            .map { regex.matchEntire(it)!!.destructured }
            .map { (axis, d) -> axis to d.toInt() }
            .toList()

        return points to folds
    }

    private fun foldPaper(points: List<Vec2>, folds: List<Pair<String, Int>>) =
        folds.fold(points) { dots, (axis, d) ->
            if (axis == "x")
                dots.map { it.copy(x = d - abs(it.x - d)) }
            else
                dots.map { it.copy(y = d - abs(it.y - d)) }

        }.toSet()

    private fun prettyString(result: Set<Vec2>) =
        (0..result.maxOf(Vec2::y)).joinToString("\n", "\n") { y ->
            (0..result.maxOf(Vec2::x)).joinToString("") { x ->
                if (Vec2(x, y) in result) "#" else " "
            }
        }
}
