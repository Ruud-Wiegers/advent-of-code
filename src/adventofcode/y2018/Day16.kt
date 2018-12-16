package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import java.util.*

fun main(args: Array<String>) {
    Day16.solve()
}

object Day16 : AdventSolution(2018, 16, "Chronal Classification") {

    override fun solvePartOne(input: String): Int {
        return parse(input).first.count { (before, instruction, after) ->
            Operation.values().count { operation ->
                operation.execute(before, instruction) == after
            } >= 3
        }
    }

    override fun solvePartTwo(input: String): Int {
        val (examples, program) = parse(input)
        val foundOpcodes = findOperationsFromExamples(examples)

        val result = program.fold(State(listOf(0,0,0,0))) { state, instruction ->
            foundOpcodes[instruction.opcode]!!.execute(state,instruction)
        }
        return result.reg[0]
    }

    private fun findOperationsFromExamples(examples: List<Example>): SortedMap<Int, Operation> {
        val foundOpcodes = sortedMapOf<Int, Operation>()
        while (foundOpcodes.size < Operation.values().size) {
            val testCases = examples.filter { it.instruction.opcode !in foundOpcodes.keys }
            val ops = Operation.values().filter { it !in foundOpcodes.values }
            testCases.forEach { ex ->
                val validOps = ops.filter { op ->
                    op.execute(ex.before, ex.instruction) == ex.after
                }
                if (validOps.size == 1) {
                    foundOpcodes[ex.instruction.opcode] = validOps.first()
                }
            }
        }
        return foundOpcodes
    }

    private fun parse(input: String): Pair<List<Example>, List<Instruction>> {
        val (ex, prog) = input.split("\n\n\n\n")
        val examples = ex.split("\n\n")

        val e = examples.map {
            val (before, op, after) = it.split("\n").map { line ->
                line.filter { it in ('0'..'9') || it == ' ' }
                        .trim()
                        .split(" ")
                        .map(String::toInt)
            }
            Example(
                    State(before),
                    Instruction(op[0], op[1], op[2], op[3]),
                    State(after)
            )

        }

        val p = prog.split('\n').map { it.split(" ").map(String::toInt).let { (a, b, c, d) -> Instruction(a, b, c, d) } }
        return (e to p)
    }

    data class Example(val before: State, val instruction: Instruction, val after: State)
    data class State(val reg: List<Int>)
    data class Instruction(val opcode: Int, val input1: Int, val input2: Int, val target: Int)

    enum class Operation {
        Addr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] + regs[data.input2]
        },
        Addi {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] + data.input2
        },
        Mulr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] * regs[data.input2]
        },
        Muli {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] * data.input2
        },
        Banr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] and regs[data.input2]
        },
        Bani {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] and data.input2
        },
        Borr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] or regs[data.input2]
        },
        Bori {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1] or data.input2
        },
        Setr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    regs[data.input1]
        },
        Seti {
            override fun execute(regs: List<Int>, data: Instruction) = data.input1
        },
        Gtir {
            override fun execute(regs: List<Int>, data: Instruction) =
                    if (data.input1 > regs[data.input2]) 1 else 0
        },
        Gtri {
            override fun execute(regs: List<Int>, data: Instruction) =
                    if (regs[data.input1] > data.input2) 1 else 0
        },
        Gtrr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    if (regs[data.input1] > regs[data.input2]) 1 else 0
        },
        Eqir {
            override fun execute(regs: List<Int>, data: Instruction) =
                    if (data.input1 == regs[data.input2]) 1 else 0
        },
        Eqri {
            override fun execute(regs: List<Int>, data: Instruction) =
                    if (regs[data.input1] == data.input2) 1 else 0
        },
        Eqrr {
            override fun execute(regs: List<Int>, data: Instruction) =
                    if (regs[data.input1] == regs[data.input2]) 1 else 0
        };

        fun execute(state: State, data: Instruction): State {
            val regs = state.reg.toMutableList()
            regs[data.target] = execute(state.reg, data)
            return state.copy(reg = regs)
        }

         protected abstract fun execute(regs: List<Int>, data: Instruction): Int
    }
}
