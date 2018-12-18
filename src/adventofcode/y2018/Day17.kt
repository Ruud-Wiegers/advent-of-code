package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day17.solve()

object Day17 : AdventSolution(2018, 17, "Reservoir Research") {

    override fun solvePartOne(input: String) =
            Reservoirs(input).apply { this.flow(Point(500, 0)) }.countWater()

    override fun solvePartTwo(input: String) =
            Reservoirs(input).apply { this.flow(Point(500, 0)) }.countStill()

    private class Reservoirs(input: String) {
        val xRange: IntRange
        val yRange: IntRange
        val map: List<CharArray>

        init {
            val clay = input.let(::parseToClayCoordinates)

            xRange = clay.minBy { it.x }!!.x..clay.maxBy { it.x }!!.x
            yRange = clay.minBy { it.y }!!.y..clay.maxBy { it.y }!!.y
            map = coordinatesToMap(clay)

        }

        fun flow(p: Point) {
            val d = Point(p.x, p.y + 1)
            if (d !in map) return

            if (map[d] == '.') {
                map[d] = '|'
                flow(d)
            }

            val l = Point(p.x - 1, p.y)
            if (map[d] in "#~" && l in map && map[l] == '.') {
                map[l] = '|'
                flow(l)
            }

            val r = Point(p.x + 1, p.y)
            if (map[d] in "#~" && r in map && map[r] == '.') {
                map[r] = '|'
                flow(r)
            }

            if (hasWalls(p)) fillLeftAndRight(p)
        }

        private fun hasWalls(source: Point): Boolean =
                hasWall(source.x downTo 0, source.y) && hasWall(source.x..xRange.last, source.y)

        private fun hasWall(xs: IntProgression, y: Int): Boolean {

            for (x in xs)
                when (map[y][x]) {
                    '#' -> return true
                    '.' -> return false
                }
            return false
        }

        private fun fillLeftAndRight(source: Point) {
            fillUntilWall(source.x downTo 0, source.y)
            fillUntilWall(source.x..xRange.last, source.y)
        }

        private fun fillUntilWall(xs: IntProgression, y: Int) {
            for (x in xs) {
                if (map[y][x] == '#') return
                map[y][x] = '~'
            }
        }

        private fun coordinatesToMap(clay: Set<Point>): List<CharArray> {

            val map = List(yRange.last + 1) {
                CharArray(xRange.last + 1) { '.' }
            }

            clay.forEach { (x, y) -> map[y][x] = '#' }
            map[0][500] = '+'
            return map
        }

        fun countWater() = map.slice(yRange).sumBy { it.count { it in "|~" } }
        fun countStill() = map.slice(yRange).sumBy { it.count { it == '~' } }

    }

    private fun parseToClayCoordinates(input: String): MutableSet<Point> {
        val clay = mutableSetOf<Point>()
        val regex = "(.).*?(\\d+).*?(\\d+).*?(\\d+).*?".toRegex()
        input.splitToSequence("\n")
                .map { regex.matchEntire(it)!!.destructured }
                .forEach { (o, a, b1, b2) ->
                    if (o == "x")
                        for (y in b1.toInt()..b2.toInt()) clay += Point(a.toInt(), y)
                    else
                        for (x in b1.toInt()..b2.toInt()) clay += Point(x, a.toInt())
                }
        return clay
    }

    data class Point(val x: Int, val y: Int)

}

private operator fun List<CharArray>.contains(p: Day17.Point) = p.y in indices && p.x in this[0].indices
private operator fun List<CharArray>.get(p: Day17.Point) = this[p.y][p.x]
private operator fun List<CharArray>.set(p: Day17.Point, c: Char) {
    this[p.y][p.x] = c
}