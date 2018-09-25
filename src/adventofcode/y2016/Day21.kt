package adventofcode.y2016

import adventofcode.AdventSolution
import nl.ruudwiegers.adventofcode.util.SimpleParser
import nl.ruudwiegers.adventofcode.util.parser

object Day21 : AdventSolution(2016, 21, "Scrambled Letters and Hash") {
	override fun solvePartOne(input: String) = scramble("abcdefgh", scramblingParser, input.split("\n"))

	override fun solvePartTwo(input: String) = scramble("fbgdceah", unscramblingParser, input.split("\n").reversed())
}

typealias StringOperation = String.() -> String

private fun scramble(initialString: String, parser: SimpleParser<StringOperation>, lines: List<String>): String =
		lines.fold(initialString) { stringToScramble, line ->
			val operation = parser.parse(line) ?: throw IllegalArgumentException("could not parse line: $line")
			operation.invoke(stringToScramble)
		}

private val scramblingParser = parser<StringOperation> {
	rule(PATTERN_SWAP_POSITION) { (p1, p2) -> { swapPositions(p1.toInt(), p2.toInt()) } }
	rule(PATTERN_SWAP_LETTER) { (ch1, ch2) -> { swapLetters(ch1[0], ch2[0]) } }
	rule(PATTERN_ROTATE_LEFT) { (distance) -> { rotateLeft(distance.toInt()) } }
	rule(PATTERN_ROTATE_RIGHT) { (distance) -> { rotateRight(distance.toInt()) } }
	rule(PATTERN_ROTATE_FOR_LETTER) { (ch) -> { rotateForLetter(ch[0]) } }
	rule(PATTERN_REVERSE) { (from, to) -> { reverse(from.toInt(), to.toInt()) } }
	rule(PATTERN_MOVE_TO) { (from, to) -> { moveTo(from.toInt(), to.toInt()) } }
}

private val unscramblingParser = parser<StringOperation> {
	rule(PATTERN_SWAP_POSITION) { (x, y) -> { swapPositions(x.toInt(), y.toInt()) } }
	rule(PATTERN_SWAP_LETTER) { (x, y) -> { swapLetters(x[0], y[0]) } }
	rule(PATTERN_ROTATE_LEFT) { (distance) -> { rotateRight(distance.toInt()) } }
	rule(PATTERN_ROTATE_RIGHT) { (distance) -> { rotateLeft(distance.toInt()) } }
	rule(PATTERN_ROTATE_FOR_LETTER) { (ch) -> { inverseRotateForLetter(ch[0]) } }
	rule(PATTERN_REVERSE) { (from, to) -> { reverse(from.toInt(), to.toInt()) } }
	rule(PATTERN_MOVE_TO) { (from, to) -> { moveTo(to.toInt(), from.toInt()) } }
}

private const val PATTERN_SWAP_POSITION = "swap position (\\d+) with position (\\d+)"
private const val PATTERN_SWAP_LETTER = "swap letter (.) with letter (.)"
private const val PATTERN_ROTATE_LEFT = "rotate left (\\d+) step.?"
private const val PATTERN_ROTATE_RIGHT = "rotate right (\\d+) step.?"
private const val PATTERN_ROTATE_FOR_LETTER = "rotate based on position of letter (.)"
private const val PATTERN_REVERSE = "reverse positions (\\d+) through (\\d+)"
private const val PATTERN_MOVE_TO = "move position (\\d+) to position (\\d+)"

private fun String.swapPositions(p1: Int, p2: Int): String = swapLetters(this[p1], this[p2])

private fun String.swapLetters(c1: Char, c2: Char): String = replace(c1, '#')
		.replace(c2, c1)
		.replace('#', c2)

private fun String.rotateLeft(delta: Int): String = drop(delta) + take(delta)

private fun String.rotateRight(delta: Int): String = takeLast(delta) + dropLast(delta)

private fun String.rotateForLetter(c1: Char): String {
	val delta = indexOf(c1)
	return rotateRight(delta)
			.rotateRight(if (delta >= 4) 2 else 1)
}

private fun String.reverse(start: Int, end: Int): String {
	val reverse = substring(start..end).reversed()
	return take(start) + reverse + drop(end + 1)
}

private fun String.moveTo(from: Int, to: Int): String {
	val char = this[from]
	val strWithoutChar = removeRange(from, from + 1)
	return strWithoutChar.take(to) + char + strWithoutChar.drop(to)
}

private fun String.inverseRotateForLetter(c: Char): String {
	val rotationDistance = indices.find { distance ->
		this == rotateRight(distance).rotateForLetter(c)
	} ?: throw IllegalArgumentException("can't invert")

	return rotateRight(rotationDistance)
}