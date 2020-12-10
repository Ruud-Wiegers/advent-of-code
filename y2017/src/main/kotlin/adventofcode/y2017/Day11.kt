package adventofcode.y2017

import adventofcode.AdventSolution
import kotlin.math.abs

object Day11 : AdventSolution(2017, 11, "Hex Ed") {
    override fun solvePartOne(input: String) = input.split(",")
        .mapNotNull { coordinatesForDirection[it] }
        .reduce { a, b -> a + b }
        .distance
        .toString()

    override fun solvePartTwo(input: String) = input.split(",")
        .mapNotNull { coordinatesForDirection[it] }
        .scan(HexCoordinates(0, 0, 0)) { a, b -> a + b }
        .map { it.distance }
        .maxOrNull()
        .toString()
}

//Cubic coordinates for hex grid. See https://www.redblobgames.com/grids/hexagons/
private data class HexCoordinates(private val x: Int, private val y: Int, private val z: Int) {

	infix operator fun plus(o: HexCoordinates) = HexCoordinates(x + o.x, y + o.y, z + o.z)

    val distance get() = listOf(x, y, z).map(::abs).maxOrNull()!!
}

private val coordinatesForDirection = mapOf(
		"n" to HexCoordinates(0, 1, -1),
		"s" to HexCoordinates(0, -1, 1),
		"ne" to HexCoordinates(1, 0, -1),
		"sw" to HexCoordinates(-1, 0, 1),
		"se" to HexCoordinates(1, -1, 0),
		"nw" to HexCoordinates(-1, 1, 0)
)

//Analogous to the Rx scan operator. A fold that returns each intermediate value.
private fun <T, R> Iterable<T>.scan(initial: R, operation: (R, T) -> R): List<R> {
	var result: R = initial
	return this.map {
		result = operation(result, it)
		result
	}
}
