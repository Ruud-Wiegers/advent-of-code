package adventofcode.y2015

import adventofcode.AdventSolution


object Day15 : AdventSolution(2015, 15, "Science for Hungry People") {

	override fun solvePartOne(input: String): String {
		val ingredients = parseInput(input)

		return partition(100)
				.map { recipe ->
					recipe.mapIndexed { i, c -> ingredients[i].scale(c) }
							.reduce { a, b -> a + b }
							.score()
				}
				.max()
				.toString()

	}

	override fun solvePartTwo(input: String): String {
		val ingredients = parseInput(input)

		return partition(100)
				.map { recipe ->
					recipe.mapIndexed { i, c -> ingredients[i].scale(c) }
							.reduce { a, b -> a + b }
				}
				.filter { it.calories == 500 }
				.map { it.score() }
				.max()
				.toString()

	}


	private fun partition(max: Int) = sequence {
		for (a in 0..max)
			for (b in 0..max - a) {
				for (c in 0..max - a - b) {
					val d = max - a - b - c
					yield(listOf(a, b, c, d))
				}
			}
	}
}

private fun parseInput(distances: String) = distances.lines()
		.mapNotNull { Regex("(\\w+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)").matchEntire(it) }
		.map { it.destructured }
		.map { (name, cap, d, f, t, cal) ->
			Ingredient(name, cap.toInt(), d.toInt(), f.toInt(), t.toInt(), cal.toInt())
		}

data class Ingredient(
		val name: String,
		val capacity: Int,
		val durability: Int,
		val flavor: Int,
		val texture: Int,
		val calories: Int
) {
	fun scale(n: Int) = Ingredient(
			this.name,
			this.capacity * n,
			this.durability * n,
			this.flavor * n,
			this.texture * n,
			this.calories * n)

	infix operator fun plus(b: Ingredient) = Ingredient("cookie",
			this.capacity + b.capacity,
			this.durability + b.durability,
			this.flavor + b.flavor,
			this.texture + b.texture,
			this.calories + b.calories)

	fun score() = this.capacity.coerceAtLeast(0) *
			this.durability.coerceAtLeast(0) *
			this.flavor.coerceAtLeast(0) *
			this.texture.coerceAtLeast(0)
	//this.durability.coerceAtLeast(0)
}
