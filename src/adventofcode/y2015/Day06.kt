package nl.ruudwiegers.adventofcode.y2015

import adventofcode.AdventSolution
import nl.ruudwiegers.adventofcode.util.SimpleParser
import nl.ruudwiegers.adventofcode.util.parser

private typealias ScreenAction = IScreen.() -> Unit

object Day06 : AdventSolution(2015, 6, "Probably a Fire Hazard") {

	override fun solvePartOne(input: String): String {
		val parser = buildParser()
		return input.split('\n')
				.map { line -> parser.parse(line) ?: throw IllegalStateException() }
				.fold(BinaryScreen(1000, 1000)) { screen, action -> screen.apply(action) }
				.litPixels()
				.toString()
	}

	override fun solvePartTwo(input: String): String {

		val parser = buildParser()

		return input.split('\n')
				.map { line -> parser.parse(line) ?: throw IllegalStateException() }
				.fold(BrightScreen(1000, 1000)) { screen, action -> screen.apply(action) }
				.totalBrightness()
				.toString()
	}


	private fun buildParser(): SimpleParser<ScreenAction> = parser {
		rule("turn on (\\d+),(\\d+) through (\\d+),(\\d+)") { (x1, y1, x2, y2) ->
			{ turnOnRange(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt()) }
		}
		rule("turn off (\\d+),(\\d+) through (\\d+),(\\d+)") { (x1, y1, x2, y2) ->
			{ turnOffRange(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt()) }
		}
		rule("toggle (\\d+),(\\d+) through (\\d+),(\\d+)") { (x1, y1, x2, y2) ->
			{ toggleRange(x1.toInt()..x2.toInt(), y1.toInt()..y2.toInt()) }
		}
	}
}

interface IScreen {
	fun turnOnRange(xs: IntRange, ys: IntRange) = doAThing(xs, ys, IScreen::turnOn)
	fun turnOffRange(xs: IntRange, ys: IntRange) = doAThing(xs, ys, IScreen::turnOff)
	fun toggleRange(xs: IntRange, ys: IntRange) = doAThing(xs, ys, IScreen::toggle)

	fun turnOn(x: Int, y: Int)
	fun turnOff(x: Int, y: Int)
	fun toggle(x: Int, y: Int)

	private fun doAThing(xs: IntRange, ys: IntRange, action: IScreen.(x: Int, y: Int) -> Unit) {
		for (x in xs)
			for (y in ys)
				action(x, y)
	}

}

class BinaryScreen(rows: Int, columns: Int) : IScreen {
	private val screen: Array<BooleanArray> = Array(rows) {
		BooleanArray(columns) { false }
	}

	override fun turnOn(x: Int, y: Int) {
		screen[y][x] = true
	}

	override fun turnOff(x: Int, y: Int) {
		screen[y][x] = false
	}

	override fun toggle(x: Int, y: Int) {
		screen[y][x] = !screen[y][x]
	}


	fun litPixels() = screen.sumBy { row -> row.count { it } }

}

class BrightScreen(rows: Int, columns: Int) : IScreen {
	private val screen: Array<IntArray> = Array(rows) {
		IntArray(columns) { 0 }
	}

	override fun turnOn(x: Int, y: Int) {
		screen[y][x] += 1
	}

	override fun turnOff(x: Int, y: Int) {
		if (screen[y][x] > 0) screen[y][x]--
	}

	override fun toggle(x: Int, y: Int) {
		screen[y][x] += 2
	}


	fun totalBrightness() = screen.sumBy { it.sum() }


}
