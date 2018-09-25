package adventofcode.y2017

import adventofcode.AdventSolution

object Day25 : AdventSolution(2017, 25, "The Halting Problem") {

	override fun solvePartOne(input: String): String {
		val (stepsUntilDiagnostic, turingMachine) = parseInstructions(input)

		repeat(stepsUntilDiagnostic) {
			turingMachine.step()
		}

		return turingMachine.tape.values.count { it == "1" }.toString()
	}

	override fun solvePartTwo(input: String): String {
		return "Free Star!"
	}

}

private fun parseInstructions(input: String): Pair<Int, TuringMachine> {
	val instructions = input.split("\n")
	val initialState = instructions[0].substringAfter("Begin in state ")
			.dropLast(1)
	val stepsUntilDiagnostic = instructions[1].substringAfter("Perform a diagnostic checksum after ")
			.substringBefore(" steps.").toInt()

	val transitionRules = (3 until instructions.size step 10)
			.flatMap { lineNumber ->
				readStateTransistions(instructions, lineNumber)
			}
			.associateBy { Pair(it.state, it.read) }

	val turingMachine = TuringMachine(initialState, transitionRules)
	return Pair(stepsUntilDiagnostic, turingMachine)
}

private fun readStateTransistions(instructions: List<String>, lineNumber: Int): List<Transition> {
	val state = instructions[lineNumber].substringAfter("In state ")
			.dropLast(1)

	val t1 = readTransition(state, instructions, lineNumber + 1)
	val t2 = readTransition(state, instructions, lineNumber + 5)
	return listOf(t1, t2)
}

private fun readTransition(state: String, instructions: List<String>, lineNumber: Int): Transition {
	val read = instructions[lineNumber].substringAfter("If the current value is ").dropLast(1)
	val write = instructions[lineNumber + 1].substringAfter("- Write the value ").dropLast(1)
	val moveDirection = instructions[lineNumber + 2].substringAfter("- Move one slot to the ").dropLast(1)
	val nextState = instructions[lineNumber + 3].substringAfter("- Continue with state ").dropLast(1)

	val move: Int = if (moveDirection == "right") 1 else -1
	return Transition(state, read, write, move, nextState)
}


private data class TuringMachine(var state: String, val transitionRules: Map<Pair<String, String>, Transition>) {
	val tape = mutableMapOf<Int, String>()
	var position = 0

	fun step() {
		val read = tape[position] ?: "0"
		val rule = transitionRules[Pair(state, read)]!!
		tape[position] = rule.write
		position += rule.move
		state = rule.nextState
	}
}

private data class Transition(val state: String, val read: String, val write: String, val move: Int, val nextState: String)
