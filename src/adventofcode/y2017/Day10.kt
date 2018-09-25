package adventofcode.y2017

import adventofcode.AdventSolution

object Day10 : AdventSolution(2017, 10, "Knot Hash") {

	override fun solvePartOne(input: String): String {
		val lengths = input.split(",").map { it.toInt() }

		val list = knot(lengths)
		return (list[0] * list[1]).toString()
	}

	override fun solvePartTwo(input: String): String {
		val denseHash = knotHash(input)
		return denseHash.joinToString("") { it.toString(16).padStart(2, '0') }
	}
}

fun knotHash(input: String): List<Int> {
	val lengths = input.map { it.toInt() } + listOf(17, 31, 73, 47, 23)
	val rounds = (1..64).flatMap { lengths }

	val sparseHash = knot(rounds)
	return sparseHash.chunked(16) { it.reduce(Int::xor) }
}

private fun knot(lengths: List<Int>): List<Int> {
	val initial = (0..255).toList()
	val list = lengths.foldIndexed(initial) { skip, list, length ->
		val knot = list.drop(length) + list.take(length).reversed()
		knot.rotateLeft(skip)
	}

	//undo all previous rotations
	val rotation = lengths.withIndex()
			.sumBy { (i, l) -> i + l } % list.size
	return list.rotateLeft(list.size - rotation)
}

private fun List<Int>.rotateLeft(n: Int) = drop(n % size) + take(n % size)
