package adventofcode.y2016

import adventofcode.AdventSolution

object Day09 : AdventSolution(2016, 9, "Explosives in Cyberspace") {
	override fun solvePartOne(input: String) = decompress(input).toString()

	override fun solvePartTwo(input: String) = decompress2(input).toString()


	private fun decompress(content: String): Int {
		var decompressedLength = 0

		var pos = 0
		val rule = ".*?((\\d+)x(\\d+)).*?".toRegex()
		while (pos < content.length) {
			val match = rule.matchEntire(content.subSequence(pos until content.length))
					?: return decompressedLength + content.length - pos

			val (_, b, r) = match.destructured
			val indexOfCompressedBlock = match.groups[1]!!.range.last + 2
			decompressedLength += b.toInt() * r.toInt()
			decompressedLength += match.groups[1]!!.range.first -1
			pos += indexOfCompressedBlock + b.toInt()
		}
		return decompressedLength
	}

	private fun decompress2(content: String): Long {
		var decompressedLength = 0L

		var pos = 0
		val rule = ".*?((\\d+)x(\\d+)).*?".toRegex()
		while (pos < content.length) {
			val match = rule.matchEntire(content.subSequence(pos until content.length))
					?: return decompressedLength + content.length - pos

			val (_, b, r) = match.destructured
			val indexOfCompressedBlock = match.groups[1]!!.range.last + 2
			val substring = content.substring(pos + indexOfCompressedBlock, pos + indexOfCompressedBlock + b.toInt())
			decompressedLength += match.groups[1]!!.range.first -1
			decompressedLength += decompress2(substring) * r.toInt()
			pos += indexOfCompressedBlock + b.toInt()
		}
		return decompressedLength
	}
}
