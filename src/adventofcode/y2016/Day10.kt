package adventofcode.y2016

import adventofcode.AdventSolution

object Day10 : AdventSolution(2016, 10, "Balance Bots") {
	override fun solvePartOne(input: String): String {
		val (chips, instructionsMap) = parse(input)


		while (chips.values.any { it.size > 1 }) {
			chips
					.filter { it.value.size > 1 }
					.forEach { (bot, ch) ->
						chips.replace(bot, emptySet())


						val lowChip = minOf(ch.first(), ch.last())
						val lowBot = instructionsMap[bot]?.lowId ?: 0
						if (instructionsMap[bot]?.lowIsRobot == true) {
							chips.merge(lowBot, setOf(lowChip)) { a, b -> a + b }
						}

						val highChip = maxOf(ch.first(), ch.last())
						val highBot = instructionsMap[bot]?.highId ?: 0
						if (instructionsMap[bot]?.highIsRobot == true) {
							chips.merge(highBot, setOf(highChip)) { a, b -> a + b }
						}
					}


			chips.entries.find { 61 in it.value && 17 in it.value }?.let { return it.key.toString() }
		}
		return "???"
	}


	override fun solvePartTwo(input: String): String {

		val (chips, instructionsMap) = parse(input)

		val outputs = mutableMapOf<Int, Int>()

		while (chips.values.any { it.size > 1 }) {
			chips
					.filter { it.value.size > 1 }
					.forEach { (bot, ch) ->
						chips.replace(bot, emptySet())


						val lowChip = minOf(ch.first(), ch.last())
						val lowBot = instructionsMap[bot]?.lowId ?: 0
						if (instructionsMap[bot]?.lowIsRobot == true) {
							chips[lowBot] = chips[lowBot].orEmpty() + lowChip
						} else {
							outputs[lowBot] = lowChip
						}

						val highChip = maxOf(ch.first(), ch.last())
						val highBot = instructionsMap[bot]?.highId ?: 0
						if (instructionsMap[bot]?.highIsRobot == true) {
							chips[highBot] = chips[highBot].orEmpty() + highChip
						} else {
							outputs[highBot] = highChip
						}
					}
		}
		return (0..2).map(outputs::getValue).reduce(Int::times).toString()
	}

	private fun parse(input: String): Pair<MutableMap<Int, Set<Int>>, Map<Int, BotInstruction>> {
		val (valueStrings, instructionStrings) = input.splitToSequence("\n").partition { it.startsWith("value") }

		val chips: MutableMap<Int, Set<Int>> = mutableMapOf()

		val valuePattern = ("value (\\d+) goes to bot (\\d+)").toRegex()
		valueStrings
				.map { valuePattern.matchEntire(it)!!.destructured }
				.map { (ch, bot) -> ValueInstruction(ch.toInt(), bot.toInt()) }
				.forEach { chips[it.receiver] = chips[it.receiver].orEmpty() + it.chip }

		val instructionPattern = ("bot (\\d+) gives low to (output|bot) (\\d+) and high to (output|bot) (\\d+)").toRegex()
		val instructionsMap = instructionStrings
				.map { instructionPattern.matchEntire(it)!!.destructured }
				.map { (b0, t1, b1, t2, b2) -> BotInstruction(b0.toInt(), t1 == "bot", b1.toInt(), t2 == "bot", b2.toInt()) }
				.associate { it.id to it }
		return Pair(chips, instructionsMap)
	}
}

sealed class Instruction
data class ValueInstruction(
		val chip: Int,
		val receiver: Int) : Instruction()

data class BotInstruction(
		val id: Int,
		val lowIsRobot: Boolean,
		val lowId: Int,
		val highIsRobot: Boolean,
		val highId: Int) : Instruction()
