package adventofcode.y2016

import adventofcode.AdventSolution


object Day07 : AdventSolution(2016, 7, "Internet Protocol Version 7") {
	override fun solvePartOne(input: String) = input.lineSequence().count(::isValidTLS).toString()
	override fun solvePartTwo(input: String) = input.lineSequence().count(::isValidSSL).toString()


	private fun isValidTLS(s: String): Boolean = splitHypernet(s).let { (main, hypernet) ->
		main.any { containsAbba(it.value) }
				&& hypernet.none { containsAbba(it.value) }
	}

	private fun isValidSSL(s: String): Boolean = splitHypernet(s).let { (main, hypernet) ->
		val babs = main.flatMap { getAbas(it.value) }
		val abas = hypernet.flatMap { getAbas(it.value) }

		return babs.any { bab ->
			abas.any { aba ->
				bab[0] == aba[1] && aba[0] == bab[1]
			}
		}
	}


	private fun splitHypernet(s: String): Pair<List<IndexedValue<String>>, List<IndexedValue<String>>> {
		return s.split('[', ']')
				.withIndex()
				.partition { it.index % 2 == 0 }
	}

	private fun containsAbba(s: String): Boolean = s
			.windowed(4)
			.any { it[0] == it[3] && it[1] == it[2] && it[0] != it[1] }

	private fun getAbas(s: String): List<String> = s
			.windowed(3)
			.filter { it[0] == it[2] && it[0] != it[1] }
}