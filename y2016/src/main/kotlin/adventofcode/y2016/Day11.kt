package adventofcode.y2016

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.IState
import adventofcode.util.algorithm.aStar

object Day11 : AdventSolution(2016, 11, "Radioisotope Thermoelectric Generators") {

	override fun solvePartOne(input: String): String = aStar(FloorPlan(1,
			parseConfig(input)))
			?.cost
			.toString()

	override fun solvePartTwo(input: String): String = aStar(FloorPlan(1,
			listOf(E(1, 1), E(1, 1)) + parseConfig(input)))
			?.cost
			.toString()
}

private fun parseConfig(input: String): List<E> {
	val chips = mutableMapOf<String, Int>()
	val generators = mutableMapOf<String, Int>()
	input.lineSequence().forEachIndexed { floor, contents ->
		contents.splitToSequence(" a ")
				.map { it.substringBefore(' ') }
				.forEach {
					if ('-' in it)
						chips[it.substringBefore('-')] = floor + 1
					else generators[it] = floor + 1
				}
	}
	return chips.map { E(it.value, generators[it.key]!!) }

}


private data class E(val chip: Int, val gen: Int) : Comparable<E> {
	override fun compareTo(other: E): Int {
		val ch = chip.compareTo(other.chip)
		return if (ch != 0) ch else gen.compareTo(other.gen)
	}

	fun moveOne(oldFloor: Int, newFloor: Int): Sequence<E> = sequence {
		if (oldFloor == chip) yield(copy(chip = newFloor))
		if (oldFloor == gen) yield(copy(gen = newFloor))
	}

	fun moveBoth(oldFloor: Int, newFloor: Int): Sequence<E> =
			if (gen == chip && oldFloor == chip) sequenceOf(copy(chip = newFloor, gen = newFloor))
			else emptySequence()
}

private data class FloorPlan(private val elevator: Int, private val elements: List<E>) : IState {
	private fun isValid(): Boolean = elevator in 1..4 &&
			elements.filterNot { it.chip == it.gen }
					.none { element ->
						elements.any { other -> element.chip == other.gen }

					}

	override fun getNeighbors(): Sequence<IState> {
		val seqUp = if (elevator in 1..3) getNeighbors(elevator + 1) else emptySequence()
		val seqDown = if (elevator in 2..4) getNeighbors(elevator - 1) else emptySequence()

		return (seqUp + seqDown).filter { it.isValid() }
	}

	private fun getNeighbors(newFloor: Int): Sequence<FloorPlan> = sequence {
		elements.forEachIndexed { index, elementOne ->
			elementOne.moveBoth(elevator, newFloor).forEach { movedPair ->
				yield(copy(elevator = newFloor, elements = (elements - elementOne + movedPair).sorted()))
			}
			elementOne.moveOne(elevator, newFloor).forEach { movedElementOne ->
				yield(copy(elevator = newFloor, elements = (elements - elementOne + movedElementOne).sorted()))
				elements.filterIndexed { indexTwo, _ -> indexTwo > index }.forEach { elementTwo ->
					elementTwo.moveOne(elevator, newFloor).forEach { movedElementTwo ->
						yield(copy(elevator = newFloor, elements = (elements - elementOne - elementTwo + movedElementOne + movedElementTwo).sorted()))
					}
				}
			}
		}
	}


	override val isGoal = elements.all { it.chip == 4 && it.gen == 4 }

	override val heuristic = elements.sumOf { 8 - it.chip - it.gen } / 2

}
