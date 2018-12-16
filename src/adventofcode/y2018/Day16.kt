package adventofcode.y2018

import adventofcode.AdventSolution

object Day16 : AdventSolution(2018, 16, "Chronal Classification") {

    override fun solvePartOne(input: String) =
            parse(input).first.count { example ->
                allOperations.count { operation -> operation.solves(example) } >= 3
            }

    override fun solvePartTwo(input: String): Int {
        val (examples, program) = parse(input)
        val operations = findOperationsFromExamples(examples)

        val initial = listOf(0, 0, 0, 0)
        val result = program.fold(initial) { registers, instruction ->
            operations[instruction.opcode].execute(registers, instruction)
        }

        return result[0]
    }

    private fun findOperationsFromExamples(examples: Sequence<Example>): List<Operation> {
        val ambiguousOpcodes = allOperations.indices.associateWith { allOperations.toMutableSet() }.toSortedMap()
        val foundOpcodes = sortedMapOf<Int, Operation>()

        examples
                .takeWhile { ambiguousOpcodes.isNotEmpty() }
                .forEach { example ->

                    //the opcode of the example can only refer to operations that solves the example
                    ambiguousOpcodes[example.instruction.opcode]?.retainAll { it.solves(example) }

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

    private fun parse(input: String): Pair<Sequence<Example>, Sequence<Instruction>> {
        val (partOne, partTwo) = input.split("\n\n\n\n")

        val examples = partOne.splitToSequence("\n\n")
                .map {
                    val (before, op, after) = it.split("\n").map { line ->
                        line.filter { c -> c in ('0'..'9') || c == ' ' }
                                .trim()
                                .split(" ")
                                .map(String::toInt)
                    }
                    Example(
                            before,
                            Instruction(op[0], op[1], op[2], op[3]),
                            after
                    )

                }

        val program = partTwo.splitToSequence('\n')
                .map {
                    it.split(" ")
                            .map(String::toInt)
                            .let { (a, b, c, d) -> Instruction(a, b, c, d) }
                }

        return (examples to program)
    }
}

private data class Example(val before: Registers, val instruction: Instruction, val after: Registers)
private data class Instruction(val opcode: Int, val input1: Int, val input2: Int, val target: Int)
private typealias Registers = List<Int>

private typealias Operation = (r: Registers, a: Int, b: Int) -> Int

private fun Operation.execute(registers: Registers, instruction: Instruction): Registers =
        registers.toMutableList().also { regs ->
            regs[instruction.target] = this(registers, instruction.input1, instruction.input2)
        }

private fun Operation.solves(example: Example): Boolean =
        execute(example.before, example.instruction) == example.after

//sadly, there's no concise way to name these
private val allOperations = listOf<Operation>(
        { r, a, b -> r[a] + r[b] },
        { r, a, b -> r[a] + b },
        { r, a, b -> r[a] * r[b] },
        { r, a, b -> r[a] * b },
        { r, a, b -> r[a] and r[b] },
        { r, a, b -> r[a] and b },
        { r, a, b -> r[a] or r[b] },
        { r, a, b -> r[a] or b },
        { r, a, _ -> r[a] },
        { _, a, _ -> a },
        { r, a, b -> if (a > r[b]) 1 else 0 },
        { r, a, b -> if (r[a] > b) 1 else 0 },
        { r, a, b -> if (r[a] > r[b]) 1 else 0 },
        { r, a, b -> if (a == r[b]) 1 else 0 },
        { r, a, b -> if (r[a] == b) 1 else 0 },
        { r, a, b -> if (r[a] == r[b]) 1 else 0 }
)
