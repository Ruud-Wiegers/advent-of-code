package adventofcode.y2016

import adventofcode.AdventSolution
import adventofcode.util.parser


object Day08 : AdventSolution(2016, 8, "Two-Factor Authentication") {

	override fun solvePartOne(input: String) = solve(input).let { screen ->
		screen.screen.sumBy { row -> row.count { it } }.toString()
	}

	override fun solvePartTwo(input: String) = solve(input).let { screen ->
		screen.screen.joinToString("") { row ->
			"\n" + row.joinToString("") { pixel -> if (pixel) "█" else " " }
		}
	}

	private fun solve(input: String): Screen {


		val parser = parser<(Screen) -> Unit> {
			rule("rect (\\d+)x(\\d+)") { (x, y) ->
				{ it.drawRectangle(x.toInt(), y.toInt()) }
			}
			rule("rotate row y=(\\d+) by (\\d+)") { (y, distance) ->
				{ it.rotateRow(y.toInt(), distance.toInt()) }
			}
			rule("rotate column x=(\\d+) by (\\d+)") { (x, distance) ->
				{ it.rotateColumn(x.toInt(), distance.toInt()) }
			}
		}


		return Screen().apply {
			input.splitToSequence("\n").forEach { line ->
				val command = parser.parse(line) ?: throw IllegalStateException()
				command(this)
			}
		}
	}


	private class Screen(private val rows: Int = 6, private val columns: Int = 50) {

		val screen: Array<BooleanArray> = Array(rows) {
			BooleanArray(columns) { false }
		}

		fun drawRectangle(width: Int, height: Int) {
			for (x in 0 until width)
				for (y in 0 until height)
					screen[y][x] = true

		}

		fun rotateRow(y: Int, distance: Int) {
			val rotatedRow = BooleanArray(columns) { screen[y][(it + columns - distance) % columns] }
			rotatedRow.forEachIndexed { x, value -> screen[y][x] = value }
		}

		fun rotateColumn(x: Int, distance: Int) {
			val rotatedColumn = BooleanArray(rows) { screen[(it + rows - distance) % rows][x] }
			rotatedColumn.forEachIndexed { y, value -> screen[y][x] = value }
		}

	}
}


