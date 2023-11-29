package adventofcode.y2015


import adventofcode.io.AdventSolution
import java.io.StreamTokenizer
import java.io.StringReader

object Day12 : AdventSolution(2015, 12, "JSAbacusFramework.io") {

	override fun solvePartOne(input: String) = traverse(input.asJson()).toString()
	override fun solvePartTwo(input: String) = traverseNonRed(input.asJson()).toString()

	private fun String.asJson() = parse(tokenize(this))

	private fun traverse(node: Any): Int = when (node) {
		is Int -> node
		is List<*> -> traverseAndSum(node, this::traverse)
		is Map<*, *> -> traverseAndSum(node.values, this::traverse)
		else -> 0
	}

	private fun traverseNonRed(node: Any): Int = when {
		node is Int -> node
		node is List<*> -> traverseAndSum(node, this::traverseNonRed)
		node is Map<*, *> && !node.containsValue("red") -> traverseAndSum(node.values, this::traverseNonRed)
		else -> 0
	}

	private inline fun traverseAndSum(node: Iterable<*>, traverse: (Any) -> Int) =
		node.filterNotNull().sumOf { traverse(it) }
}


private enum class Token { ArrayBegin, ArrayEnd, ObjectBegin, ObjectEnd, Separator, Comma }

private fun tokenize(str: String): Iterator<Any> = iterator {
	val reader = StringReader(str)
	val tokenizer = StreamTokenizer(reader)
	while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
		val tokenValue: Any = when (tokenizer.ttype)
		{
			StreamTokenizer.TT_NUMBER -> tokenizer.nval.toInt()
			'"'.code                  -> tokenizer.sval
			'['.code -> Token.ArrayBegin
			']'.code -> Token.ArrayEnd
			'{'.code -> Token.ObjectBegin
			'}'.code -> Token.ObjectEnd
			':'.code -> Token.Separator
			','.code -> Token.Comma
			else     -> throw IllegalArgumentException()
		}
		yield(tokenValue)
	}
}

private fun parse(iter: Iterator<Any>): Any =
		iter.next().let { token ->
			when (token) {
				Token.ArrayBegin -> parseArray(iter)
				Token.ObjectBegin -> parseObject(iter)
				is Int -> token
				is String -> token
				else -> throw IllegalArgumentException()
			}
		}

private fun parseArray(iter: Iterator<Any>): List<Any> =
		mutableListOf<Any>().apply {
			do {
				this.add(parse(iter))
			} while (iter.next() == Token.Comma)
		}

private fun parseObject(iter: Iterator<Any>): Map<String, Any> =
		mutableMapOf<String, Any>().apply {
			do {
				val k = iter.next() as String
				assert(iter.next() == Token.Separator)
				val v = parse(iter)
				this[k] = v
			} while (iter.next() == Token.Comma)
		}
