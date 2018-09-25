package adventofcode.y2016

import adventofcode.y2016.Day01.Orientation.*
import adventofcode.AdventSolution
import kotlin.math.abs

object Day01 : AdventSolution(2016, 1, "No Time for a Taxicab") {

	override fun solvePartOne(input: String) =
			input.splitToSequence(", ")
					.map { Pair(it[0] == 'L', it.substring(1).toInt()) }
					.fold(Position(North, 0, 0)) { pos, (isLeft, steps) ->
						if (isLeft) {
							pos.left()
						} else {
							pos.right()
						}.step(steps)
					}
					.distance()
					.toString()

	override fun solvePartTwo(input: String): String {
		val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
		return enumeratePosition(input)
				.map { it.x to it.y }
				.first { pos ->
					val isDuplicate = pos in visited
					visited += pos
					isDuplicate
				}
				.let { (x, y) -> abs(x) + abs(y) }
				.toString()
	}


	private fun enumeratePosition(input: String) = sequence {
		input.splitToSequence(", ")
				.map { Pair(it[0] == 'L', it.substring(1).toInt()) }
				.fold(Position(North, 0, 0)) { pos, (isLeft, steps) ->
					var t = if (isLeft) {
						pos.left()
					} else {
						pos.right()
					}
					repeat(steps) {
						t = t.step(1)
						yield(t)
					}
					t
				}
	}


	data class Position(val facing: Orientation, val x: Int, val y: Int) {

		fun left(): Position = copy(facing = facing.left())

		fun right(): Position = copy(facing = facing.right())

		fun step(d: Int): Position = when (facing) {
			North -> copy(y = y + d)
			East -> copy(x = x + d)
			South -> copy(y = y - d)
			West -> copy(x = x - d)
		}

		fun distance() = abs(x) + abs(y)
	}

	enum class Orientation {
		North, East, South, West;

		fun left() = when (this) {
			North -> West
			East -> North
			South -> East
			West -> South
		}

		fun right() = when (this) {
			North -> East
			East -> South
			South -> West
			West -> North
		}
	}

}