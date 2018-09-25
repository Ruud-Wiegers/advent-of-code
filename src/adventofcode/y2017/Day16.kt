package adventofcode.y2017

import adventofcode.AdventSolution

object Day16 : AdventSolution(2017, 16, "Permutation Promenade") {

	override fun solvePartOne(input: String): String = dance(parseInput(input), "abcdefghijklmnop")


	//There's actually a small upper bound for the loop size! Landau's Function
	override fun solvePartTwo(input: String): String {
		val danceMoves: List<Action> = parseInput(input)
		val orderings = generateSequence("abcdefghijklmnop") { dance(danceMoves, it) }
				.takeWhileDistinct()
				.toList()

		val remainder = (1_000_000_000 % orderings.size)

		return orderings[remainder]
	}

	private fun parseInput(input: String): List<Action> = input.split(",")
			.map { move ->
				when (move[0]) {
					's' -> Action.Spin(move.substring(1).toInt())
					'x' -> move.substring(1).split('/').let { Action.Exchange(it[0].toInt(), it[1].toInt()) }
					'p' -> Action.Promenade(move[1], move[3])
					else -> throw IllegalArgumentException("$move is not a dance move")
				}
			}

	private fun dance(moves: List<Action>, initialPositions: String) =
			moves.fold(initialPositions) { acc, action -> action.execute(acc) }
}

private sealed class Action {
	abstract fun execute(programs: String): String

	class Spin(private val n: Int) : Action() {
		override fun execute(programs: String) = programs.takeLast(n) + programs.dropLast(n)
	}

	class Exchange(private val i1: Int, private val i2: Int) : Action() {
		override fun execute(programs: String): String {
			val exchanged = programs.toCharArray()
			exchanged[i1] = programs[i2]
			exchanged[i2] = programs[i1]
			return String(exchanged)
		}
	}

	class Promenade(private val p1: Char, private val p2: Char) : Action() {
		override fun execute(programs: String): String {
			val promenaded = programs.toCharArray()
			val i1 = programs.indexOf(p1)
			val i2 = programs.indexOf(p2)
			promenaded[i1] = programs[i2]
			promenaded[i2] = programs[i1]
			return String(promenaded)
		}
	}
}
