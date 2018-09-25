package nl.ruudwiegers.adventofcode.util


class SimpleParser<RESULT> {
	private val children = arrayListOf<ParseRule<RESULT>>()

	fun rule(regex: String, converter: (MatchResult.Destructured) -> RESULT) {
		children += ParseRule(regex.toRegex(), converter)
	}

	fun parse(text: String) = children
			.asSequence()
			.map { it -> it.parse(text) }
			.firstOrNull { it != null }
}

fun <R> parser(init: SimpleParser<R>.() -> Unit): SimpleParser<R> {
	val parser = SimpleParser<R>()
	parser.init()
	return parser
}

class ParseRule<out R>(private val regex: Regex, private val convertToResult: (MatchResult.Destructured) -> R) {

	fun parse(text: String): R? {
		val matchResult = regex.matchEntire(text)
		if (matchResult != null) {
			val captureGroups = matchResult.destructured
			return convertToResult(captureGroups)
		}
		return null
	}

}
