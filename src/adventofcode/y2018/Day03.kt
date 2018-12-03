package adventofcode.y2018

import adventofcode.AdventSolution

object Day03 : AdventSolution(2018, 3, "No Matter How You Slice It") {

	override fun solvePartOne(input: String): String {

		val rule = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)".toRegex()
		val claims = input.splitToSequence("\n")
				.map { rule.matchEntire(it)!!.destructured }
				.map { (id, x, y, w, h) -> Fabric(id, x.toInt(), y.toInt(), w.toInt(), h.toInt()) }

		val fabric = ShortArray(1_000_000)

		claims.forEach { claim ->
			for (x in claim.x until claim.x + claim.w)
				for (y in claim.y until claim.y + claim.h)
					fabric[1000 * y + x]++

		}

		return fabric.count { it > 1 }.toString()
	}

	override fun solvePartTwo(input: String): String {
		val rule = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)".toRegex()
		val claims = input.splitToSequence("\n")
				.map { rule.matchEntire(it)!!.destructured }
				.map { (id, x, y, w, h) -> Fabric(id, x.toInt(), y.toInt(), w.toInt(), h.toInt()) }

		val fabric = ShortArray(1_000_000)

		claims.forEach { claim ->
			for (x in claim.x until claim.x + claim.w)
				for (y in claim.y until claim.y + claim.h)
					fabric[1000 * y + x]++

		}

		return claims.first { claim ->
			for (x in claim.x until claim.x + claim.w)
				for (y in claim.y until claim.y + claim.h)
					if (fabric[1000 * y + x] > 1) return@first false
			true
		}.id
	}
}

private data class Fabric(
		val id: String,
		val x: Int,
		val y: Int,
		val w: Int,
		val h: Int)
