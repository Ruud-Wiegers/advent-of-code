package adventofcode.y2017

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() =Day14.solve()

object Day14 : AdventSolution(2017, 14, "Disk Defragmentation") {

	override fun solvePartOne(input: String): String = (0..127).asSequence()
			.map { "$input-$it" }
			.flatMap { knotHash(it).asSequence() }
			.flatMap { it.toBits().asSequence() }
			.count { it }
			.toString()

	override fun solvePartTwo(input: String): String {

		val rows = (0..127)
				.map { "$input-$it" }
				.map { knotHash(it) }
				.map { hash -> hash.flatMap { it.toBits() }.toBooleanArray() }
				.toTypedArray()

		return rows.indices.asSequence().flatMap { y -> rows[0].indices.asSequence().map { x -> x to y } }
				.filter { (x, y) -> rows[y][x] }
				.onEach { (x, y) -> clearGroup(rows, x, y) }
				.count()
				.toString()
	}

	private fun clearGroup(rows: Array<BooleanArray>, x: Int, y: Int) {
		if (rows.getOrNull(y)?.getOrNull(x) != true) {
			return
		}

		rows[y][x] = false

		clearGroup(rows, x + 1, y)
		clearGroup(rows, x - 1, y)
		clearGroup(rows, x, y + 1)
		clearGroup(rows, x, y - 1)
	}
}

private fun Int.toBits() = List(8) { bit -> this and (1 shl 7 - bit) != 0 }
