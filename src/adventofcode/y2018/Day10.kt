package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.abs

fun main() = Day10.solve()

object Day10 : AdventSolution(2018, 10, "The Stars Align") {

    override fun solvePartOne(input: String) {
        parse(input).let { solve(it) }
    }

    override fun solvePartTwo(input: String) = parse(input).let { "" }


    private fun parse(input: String) = input
            .splitToSequence("\n").map { line ->
                line
                        .split("position=<", ",", "> velocity=<", ">")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .map { it.toInt() }
            }
            .map { (px, py, vx, vy) -> Point(px, py) to Point(vx, vy) }
            .toList()
}

private fun solve(input: List<Pair<Point, Point>>) {
    val sky = generateSequence (input){it.map { (p, v) -> p + v to v }}.map { it.map { it.first } }

    val region = sky.withIndex().dropWhile { it.value.any {  p -> p.x < 0 || p.y < 0 }}
            .takeWhile {  it.value.all {  p -> p.x >= 0 || p.y >= 0 }}
            .maxBy { it.value.sumBy { a-> it.value.count { b-> a.adjacent(b) } } }!!
    println(region.index)
for (y in 105..117) {
    for (x in 105..170)
        if (Point(x, y) in region.value)
            print("#")
        else
            print(" ")
    println()
}

}

private data class Point(val x: Int, val y: Int) {
    operator fun plus(o: Point) = Point(x + o.x, y + o.y)
    fun adjacent(o:Point) = abs(x-o.x) + abs(y-o.y) <2
}


