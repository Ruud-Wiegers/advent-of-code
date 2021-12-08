package adventofcode.y2015

import adventofcode.AdventSolution


object Day16 : AdventSolution(2015, 16, "Aunt Sue") {

	override fun solvePartOne(input: String) = parseInput(input)
			.withIndex()
			.filter {
				it.value.all { (k, v) ->
					this.actualAunt[k] == v
				}
			}
			.map { it.index + 1 }
			.first().toString()


	override fun solvePartTwo(input: String) = parseInput(input)
			.withIndex()
			.filter {
				it.value.all { (k, v) ->
					when (k) {
						"cats", "trees" -> this.actualAunt[k]!! < v
						"pomeranians", "goldfish" -> this.actualAunt[k]!! > v
						else -> this.actualAunt[k] == v
					}
				}
			}
			.map { it.index + 1 }
			.first().toString()

	private fun parseInput(input: String) = input.lineSequence()
			.map {
                it.substringAfter(":")
                    .split(',').associate { i -> parseItem(i) }
            }

	private fun parseItem(item: String) = item.substringBefore(':').trim() to
			item.substringAfter(":").trim().toInt()


	private val actualAunt = mapOf("children" to 3,
			"cats" to 7,
			"samoyeds" to 2,
			"pomeranians" to 3,
			"akitas" to 0,
			"vizslas" to 0,
			"goldfish" to 5,
			"trees" to 3,
			"cars" to 2,
			"perfumes" to 1)

}