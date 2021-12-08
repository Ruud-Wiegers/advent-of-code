package adventofcode.y2017

import adventofcode.AdventSolution
import adventofcode.y2017.Day22.Health.*

object Day22 : AdventSolution(2017, 22, "Sporifica Virus") {

	override fun solvePartOne(input: String): String {
		val map: MutableMap<Point, Health> = parseInput(input)

		val weakWorm = object : Worm() {
			override fun action(health: Health): Pair<Health, Direction> =
					when (health) {
						INFECTED -> Pair(HEALTHY, direction.right())
						else -> Pair(INFECTED, direction.left())
					}
		}

		repeat(10000) { weakWorm.burst(map) }
		return weakWorm.infectionsCount.toString()
	}

	override fun solvePartTwo(input: String): String {
		val map: MutableMap<Point, Health> = parseInput(input)

		val strongWorm = object : Worm() {
			override fun action(health: Health): Pair<Health, Direction> =
					Pair(health.next(), when (health) {
						HEALTHY -> direction.left()
						WEAKENED -> direction
						INFECTED -> direction.right()
						FLAGGED -> direction.reverse()
					})
		}

		repeat(10_000_000) { strongWorm.burst(map) }
		return strongWorm.infectionsCount.toString()
	}

	abstract class Worm {
		private var position = Point(0, 0)
		protected var direction = Direction.UP
		var infectionsCount = 0

		fun burst(map: MutableMap<Point, Health>) {
			val (newHealth, newDirection) = action(map[position] ?: HEALTHY)

			map[position] = newHealth
			direction = newDirection
			position += direction.vector
			if (newHealth == INFECTED) infectionsCount++
		}

		protected abstract fun action(health: Health): Pair<Health, Direction>
	}

	private fun parseInput(input: String): MutableMap<Point, Health> = input.lines()
			.mapIndexed { y, row ->
				row.mapIndexed { x, char ->
					val position = Point(x - row.length / 2, y - row.length / 2)
					val state = if (char == '#') INFECTED else HEALTHY
					position to state
				}
			}
			.flatten().toMap().toMutableMap()

	enum class Health {
		HEALTHY, WEAKENED, INFECTED, FLAGGED;

		fun next() = values().let { it[(it.indexOf(this) + 1) % it.size] }
	}

	enum class Direction(val vector: Point) {
		UP(Point(0, -1)), RIGHT(Point(1, 0)), DOWN(Point(0, 1)), LEFT(Point(-1, 0));

		fun left() = turn(3)
		fun right() = turn(1)
		fun reverse() = turn(2)

		private fun turn(i: Int): Direction = values().let { it[(it.indexOf(this) + i) % it.size] }
	}

	data class Point(val x: Int, val y: Int) {
		operator fun plus(o: Point) = Point(x + o.x, y + o.y)
	}
}
