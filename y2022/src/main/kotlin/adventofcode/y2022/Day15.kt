package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2
import kotlin.math.abs


object Day15 : AdventSolution(2022, 15, "Beacon Exclusion Zone") {

    override fun solvePartOne(input: String) = solvePartOne(input, 2_000_000)

    fun solvePartOne(input: String, y: Int): Int {
        val signals = parse(input).toList()
        val intervals = signals.map { it.lineAt(y) }
            .filter { it.first <= it.last }
            .sortedBy { it.first }

        val beacons = signals.mapNotNull { it.beaconAt(y) }.distinct()

        val cuttingPoints = intervals.flatMap { listOf(it.first, it.last + 1) }
        val slices = intervals.flatMap { range ->
            val points = cuttingPoints.filter { it in range }.sorted() + (range.last + 1)
            points.zipWithNext(Int::until)
        }
            .distinct()


        return slices.sumOf { it.last - it.first + 1 } - beacons.count()
    }

    override fun solvePartTwo(input: String): Long {
        //rotate the grid 45 degrees. now all areas are squares!
        val areas = parse(input).map { Diamond(it.sensor, it.dist).rotate() }.toList()


        //sort all x- and y-boundary values
        val xs = areas.flatMap { (a, b) -> listOf(a.x, b.x + 1) }.toSortedSet().toList()
        val ys = areas.flatMap { (a, b) -> listOf(a.y, b.y + 1) }.toSortedSet().toList()

        //use indices instead of the huge actual values
        val ixs = xs.withIndex().associate { it.value to it.index }
        val iys = ys.withIndex().associate { it.value to it.index }

        //draw a small grid using the indices
        val grid = Array(ys.size - 1) { BooleanArray(xs.size - 1) { false } }

        //for each area, cross out the covered indices in the grid
        areas.forEach { a ->
            (iys.getValue(a.topLeft.y) until iys.getValue(a.bottomRight.y + 1)).forEach { y ->
                (ixs.getValue(a.topLeft.x) until ixs.getValue(a.bottomRight.x + 1)).forEach { x ->
                    grid[y][x] = true
                }
            }
        }

        //uncovered 1x1 areas. only one is correct, the others will be outside the original scanning boundary
        val candidates = (0 until ys.lastIndex).asSequence()
            .flatMap { iy ->
                (0 until xs.lastIndex).map { ix ->
                    Vec2(ix, iy)
                }
            }
            .filter { (ix, _) -> xs[ix] + 1 == xs[ix + 1] } //1 wide
            .filter { (_, iy) -> ys[iy] + 1 == ys[iy + 1] } //1 high
            .filter { (ix, iy) -> !grid[iy][ix] } //uncovered


        //back to original coordinates
        val transformed = candidates.map { Vec2(xs[it.x], ys[it.y]) }
            .map { it.fromRotated() }

        //filter incorrect candidates
        val boundary = 4_000_000
        val answer = transformed.single { it.x in 0..boundary && it.y in 0..boundary }

        //tuning frequency
        return answer.x * 4_000_000L + answer.y
    }
}

private fun parse(input: String): Sequence<Signal> {
    val regex = Regex("""Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""")
    return input.lineSequence().map { regex.matchEntire(it)!!.groupValues.drop(1).map(String::toInt) }
        .map { (sx, sy, bx, by) -> Signal(Vec2(sx, sy), Vec2(bx, by)) }
}

private data class Signal(val sensor: Vec2, val beacon: Vec2) {

    val dist = sensor.distance(beacon)

    fun lineAt(y: Int): IntRange {
        val distanceToLine = abs(sensor.y - y)
        val remainder = dist - distanceToLine
        return (sensor.x - remainder)..(sensor.x + remainder)
    }

    fun beaconAt(y: Int): Int? = if (beacon.y == y) beacon.x else null
}

private data class Diamond(val center: Vec2, val radius: Int) {
    fun rotate() = Area(
        center.copy(y = center.y - radius).toRotated(),
        center.copy(y = center.y + radius).toRotated()
    )

}

private data class Area(val topLeft: Vec2, val bottomRight: Vec2)

//rotate the axes CCW 45 degrees (and scale)
private fun Vec2.toRotated() = Vec2(x + y, y - x)

//rotate the axes CW 45 degrees (and rescale)
private fun Vec2.fromRotated() = Vec2((x - y) / 2, (y + x) / 2)
