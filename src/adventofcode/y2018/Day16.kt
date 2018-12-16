package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve

fun main(args: Array<String>) {
    Day16.solve()
}

object Day16 : AdventSolution(2018, 16, "Chronal Classification") {
    override fun solvePartOne(input: String) = parse(input).first.count { allOperations.count(it::isSolvedBy) >= 3 }

    override fun solvePartTwo(input: String): Int {
        val (examples, program) = parse(input)
        val foundOpcodes = findOperationsFromExamples(examples)

        val result = program.fold(listOf(0, 0, 0, 0)) { state, instruction ->
            foundOpcodes[instruction.opcode].execute(state, instruction)
        }
        return result[0]
    }

    private fun findOperationsFromExamples(examples: List<Example>): List<Operation> {
        val unknownOpcodes = allOperations.indices.associateWith { allOperations.toMutableSet() }.toSortedMap()
        val foundOpcodes = sortedMapOf<Int,Operation>()
        while (unknownOpcodes.isNotEmpty()) {
            var count = 0
            examples
                    .asSequence()
                    .takeWhile { unknownOpcodes.isNotEmpty() }
                    .filter { unknownOpcodes[it.instruction.opcode] != null }
                    .forEach { example ->
                        val opcode = example.instruction.opcode
                        val ops = unknownOpcodes[opcode]!!
                        ops.retainAll { example.isSolvedBy(it) }
                        if (ops.size == 1) {
                            val operation = ops.first()
                            unknownOpcodes.remove(example.instruction.opcode)
                            unknownOpcodes.values.forEach { it.remove(operation) }
                            foundOpcodes[example.instruction.opcode] = operation
                            println(count)
                        }
                        count++
                    }
        }
        return foundOpcodes.values.toList()
    }

    private fun parse(input: String): Pair<List<Example>, List<Instruction>> {
        val (ex, prog) = input.split("\n\n\n\n")
        val examples = ex.split("\n\n")

        val e = examples.map {
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

        val p = prog.split('\n').map { it.split(" ").map(String::toInt).let { (a, b, c, d) -> Instruction(a, b, c, d) } }
        return (e to p)
    }
}

private typealias Operation = (r: Registers, a: Int, b: Int) -> Int

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


private fun Operation.execute(registers: Registers, instruction: Instruction): Registers =
        registers.toMutableList().also { regs ->
            regs[instruction.target] = this(registers, instruction.input1, instruction.input2)
        }


private data class Example(val before: Registers, val instruction: Instruction, val after: Registers) {
    fun isSolvedBy(op: Operation) = op.execute(before, instruction) == after
}

private typealias Registers = List<Int>

private data class Instruction(val opcode: Int, val input1: Int, val input2: Int, val target: Int)
