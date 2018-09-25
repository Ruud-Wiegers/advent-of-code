package nl.ruudwiegers.adventofcode.y2015

import adventofcode.AdventSolution

object Day19 : AdventSolution(2015, 19, "Medicine for Rudolph") {

	override fun solvePartOne(input: String) = parse(input)
			.let { (ts, m) -> newMolecules(m, ts) }
			.count()
			.toString()

	override fun solvePartTwo(input: String): String {
		val (transitions, molecule) = parse(input)

		val c = molecule.count { it.isUpperCase() } - molecule.count("Rn") - molecule.count("Ar") - 2 * molecule.count("Y") - 1
		return c.toString()
	}

	private fun String.count(literal: String) = literal.toRegex().findAll(this).count()


	private fun parse(input: String): Pair<Map<String, Set<String>>, String> {
		val (rewriteRules, other) = input.split("\n").partition { "=>" in it }

		val transitions = rewriteRules.map { it.substringBefore(" =>") to it.substringAfter("=> ") }
				.groupBy({ it.first }, { it.second }).mapValues { it.value.toSet() }
		val molecule = other.last()
		return transitions to molecule
	}


	private fun newMolecules(molecule: String, transitions: Map<String, Set<String>>) =
			transitions.flatMap { (source, targets) ->
				Regex.fromLiteral(source).findAll(molecule).toList().flatMap { matchResult ->
					targets.map { t -> molecule.replaceRange(matchResult.range, t) }
				}
			}.toSet()

}




