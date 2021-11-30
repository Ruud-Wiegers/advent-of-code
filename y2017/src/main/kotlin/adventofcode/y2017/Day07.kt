package adventofcode.y2017

import adventofcode.AdventSolution

object Day07 : AdventSolution(2017, 7, "Recursive Circus") {

	override fun solvePartOne(input: String): String {
		return findRootProgram(parse(input)).name
	}

	override fun solvePartTwo(input: String): String {
		val rows = parse(input)
		val root = findRootProgram(rows)
		val rootProgram = toLinkedProgramTree(rows, root)

		val unbalancedSequence = generateSequence(rootProgram) { prev ->
			prev.children.find { it.isUnbalanced }
		}
		val parent = unbalancedSequence.last()

		val expectedWeightOfChildren = parent.children
				.groupingBy { it.combinedWeight }
				.eachCount()
				.entries
				.find { a -> a.value > 1 }
				?.key!!

		val programWithIncorrectWeight = parent.children
				.find { it.combinedWeight != expectedWeightOfChildren }!!

		return (programWithIncorrectWeight.weight
				+ expectedWeightOfChildren
				- programWithIncorrectWeight.combinedWeight).toString()
	}

	private fun parse(input: String) = input
			.lines()
			.map { UnlinkedProgram(it) }

	private fun findRootProgram(programs: List<UnlinkedProgram>) =
			sequenceOfAncestors(programs.first(), programs).last()

	private fun sequenceOfAncestors(start: UnlinkedProgram,
									programs: List<UnlinkedProgram>) =
			generateSequence(start) { prev ->
				programs.find { program ->
					prev.name in program.children
				}
			}

}

//Parsing an input row to a program description. No linking between rows
private data class UnlinkedProgram(val name: String,
								   val weight: Int, val
								   children: List<String>) {
	constructor(input: String) : this(
			name = input.substringBefore(" ("),
			weight = input.substringAfter("(")
					.substringBefore(")")
					.toInt(),
			children = input.substringAfter("-> ", "")
					.split(", ")
					.filterNot { it.isBlank() })
}

private fun toLinkedProgramTree(programs: List<UnlinkedProgram>,
								root: UnlinkedProgram): Program {
	return Program(root.name, root.weight, root.children.map { childName ->
		toLinkedProgramTree(programs, programs.find { it.name in childName }!!)
	})
}

private data class Program(val name: String,
						   val weight: Int,
						   val children: List<Program>) {

	val combinedWeight: Int by lazy {
		weight + children.sumOf { it.combinedWeight }
	}

	val isUnbalanced: Boolean by lazy {
		children.distinctBy { it.combinedWeight }.size > 1
	}
}
