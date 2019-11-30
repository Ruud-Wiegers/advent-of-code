package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.util.elfcode.*

object Day16 : AdventSolution(2018, 16, "Chronal Classification") {

    override fun solvePartOne(input: String) =
            parse(input).first.count { example ->
                allOperations.count { operation -> example.isSolvedBy(operation) } >= 3
            }

    override fun solvePartTwo(input: String): Int {
        val (examples, program) = parse(input)
        val operations = findOperationsFromExamples(examples)

        val registers = IntArray(4)
        for (instruction in program) {
            registers.execute(operations[instruction.opcode], instruction)
        }
        return registers[0]
    }

    private fun findOperationsFromExamples(examples: Sequence<Example>): List<Operation> {
        val ambiguousOpcodes = allOperations.indices.associateWith { allOperations.toMutableSet() }.toSortedMap()
        val foundOpcodes = sortedMapOf<Int, Operation>()

        examples
                .takeWhile { ambiguousOpcodes.isNotEmpty() }
                .forEach { example ->

                    //the opcode of the example can only refer to operations that isSolvedBy the example
                    ambiguousOpcodes[example.instruction.opcode]?.retainAll { example.isSolvedBy(it) }

                    //remove all found opcodes/operations from the search.
                    //this can result in determining other opcodes/operations, so iterate.
                    while (ambiguousOpcodes.any { it.value.size == 1 }) {

                        //find all determined opcodes
                        ambiguousOpcodes.keys.filter { ambiguousOpcodes[it]!!.size == 1 }
                                .forEach { opCode ->
                                    //move to found codes
                                    val operation = ambiguousOpcodes.remove(opCode)!!.first()
                                    foundOpcodes[opCode] = operation

                                    //no other opcode can refer to this operator
                                    ambiguousOpcodes.values.forEach { it -= operation }
                                }
                    }
                }

        //the map is sorted by opcode, so this results in a correctly indexed list of operators
        return foundOpcodes.values.toList()
    }

    private fun parse(input: String): Pair<Sequence<Example>, Sequence<UnboundInstruction>> {
        val (partOne, partTwo) = input.split("\n\n\n\n")

        val examples = partOne.splitToSequence("\n\n")
                .map {
                    val (before, op, after) = it.lines().map { line ->
                        line.filter { c -> c in ('0'..'9') || c == ' ' }
                                .trim()
                                .split(" ")
                                .map(String::toInt)
                                .toIntArray()
                    }
                    Example(
                            before,
                            UnboundInstruction(op[0], op[1], op[2], op[3]),
                            after
                    )

                }

        val program = partTwo.lineSequence()
                .map {
                    it.split(" ")
                            .map(String::toInt)
                            .let { (a, b, c, d) -> UnboundInstruction(a, b, c, d) }
                }

        return (examples to program)
    }
}

private data class Example(val before: Registers, val instruction: UnboundInstruction, val after: Registers) {
    fun isSolvedBy(op: Operation) = before.clone().apply { execute(op, instruction) }.contentEquals(after)
}