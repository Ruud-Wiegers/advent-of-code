package adventofcode.y2018

import adventofcode.AdventSolution

object Day17 : AdventSolution(2018, 17, "Reservoir Research") {

    override fun solvePartOne(input: String) = Reservoirs(input)
            .apply { flow(Point(500, 0)) }
            .count { it in "|~" }

    override fun solvePartTwo(input: String) = Reservoirs(input)
            .apply { flow(Point(500, 0)) }
            .count { it == '~' }

    private class Reservoirs(input: String) {
        val xRange: IntRange
        val yRange: IntRange
        val map: List<CharArray>

        init {
            val clay = input.let(::parseToClayCoordinates)
            xRange = clay.minOf { it.x }..clay.maxOf { it.x }
            yRange = clay.minOf { it.y }..clay.maxOf { it.y }
            map = coordinatesToMap(clay)
        }

        fun flow(source: Point) {
            map[source.y][source.x] = '|'

            val down = Point(source.x, source.y + 1)
            if (down.y > map.lastIndex) return
            if (map[down] == '.') flow(down)

            val left = Point(source.x - 1, source.y)
            if (map[left] == '.' && map[down] in "#~") flow(left)

            val right = Point(source.x + 1, source.y)
            if (map[right] == '.' && map[down] in "#~") flow(right)

            fillRow(source)
        }

        private operator fun List<CharArray>.get(p: Point) = this[p.y][p.x]

        //only fills when we've reached the rightmost point of the row
        private fun fillRow(source: Point) {
            val row = map[source.y]
            if (row[source.x + 1] != '#') return

            val leftEnd = (source.x downTo 0).find { x -> row[x] != '|' || map[source.y + 1][x] !in "#~" } ?: return
            if (row[leftEnd] != '#') return

            (leftEnd + 1..source.x).forEach { x -> row[x] = '~' }
        }

        private fun coordinatesToMap(clay: Iterable<Point>): List<CharArray> {
            val map = List(yRange.last + 1) {
                CharArray(xRange.last + 1) { '.' }
            }

            clay.forEach { (x, y) -> map[y][x] = '#' }
            map[0][500] = '+'
            return map
        }

        fun count(predicate: (Char) -> Boolean) = map.slice(yRange).sumOf { it.count(predicate) }
    }
}

private fun parseToClayCoordinates(input: String): List<Point> {
    val regex = """([xy])=(\d+), [xy]=(\d+)..(\d+)""".toRegex()
    return input.lineSequence()
            .map { regex.matchEntire(it)!!.destructured }
            .map { (orientation, a, bStart, bEnd) ->
                (bStart.toInt()..bEnd.toInt()).map {
                    if (orientation == "x")
                        Point(a.toInt(), it)
                    else
                        Point(it, a.toInt())
                }
            }
            .flatten()
            .toList()
}

private data class Point(val x: Int, val y: Int)
