package adventofcode.util


class SimpleParser<RESULT : Any> {
	private val rules = arrayListOf<ParseRule<RESULT>>()

	fun rule(regex: String, converter: (MatchResult.Destructured) -> RESULT) {
		rules += ParseRule(regex.toRegex(), converter)
	}

	fun parse(text: String): RESULT? = rules
			.asSequence()
			.mapNotNull { it.parse(text) }
			.firstOrNull()
}

fun <R : Any> parser(init: SimpleParser<R>.() -> Unit) = SimpleParser<R>().apply(init)

class ParseRule<out R>(private val regex: Regex, private val convertToResult: (MatchResult.Destructured) -> R) {
	fun parse(text: String): R? = regex.matchEntire(text)?.destructured?.let { convertToResult(it) }
}
