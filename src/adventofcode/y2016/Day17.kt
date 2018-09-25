package adventofcode.y2016

import adventofcode.AdventSolution
import nl.ruudwiegers.adventofcode.util.IState
import nl.ruudwiegers.adventofcode.util.aStar
import nl.ruudwiegers.adventofcode.util.aStarExhaustive
import nl.ruudwiegers.adventofcode.util.md5


object Day17 : AdventSolution(2016, 17, "Two Steps Forward") {
	override fun solvePartOne(input: String): String {
		val res = aStar(Path(0, 0, "", input)) ?: throw IllegalStateException()
		return (res.IState as Path).path
	}

	override fun solvePartTwo(input: String): String {
		val res = aStarExhaustive(Path(0, 0, "", input))
		return res.last().cost.toString()
	}
}

private enum class Dir(val pos: Int, val ch: Char, val dx: Int, val dy: Int) {
	UP(0, 'U', 0, -1),
	DOWN(1, 'D', 0, 1),
	LEFT(2, 'L', -1, 0),
	RIGHT(3, 'R', 1, 0)
}

private fun open(hash: String, dir: Dir) = hash[dir.pos] in 'b'..'f'


class Path(
		private val x: Int,
		private val y: Int,
		val path: String,
		private val password: String) : IState {


	override fun getNeighbors(): Sequence<IState> {
		return Dir.values()
				.asSequence()
				.filter { open(md5(password + path), it) }
				.map(this::copy)
				.filter(Path::isValid)
	}

	private fun copy(dir: Dir) = Path(x + dir.dx, y + dir.dy, path + dir.ch, password)
	private fun isValid() = x in 0..3 && y in 0..3

	override val isGoal get() = x == 3 && y == 3
	override val heuristic get() = 6 - (x + y)
}

