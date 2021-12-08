package adventofcode.util.elfcode


fun parseToElfcode(input: String): Pair<Int, List<Instruction>> {
    val lines = input.lines()
    val ip = lines[0].substringAfter("#ip ").toInt()
    val instructions = lines
        .drop(1)
        .map {
            it.split(" ")
                .let { (a, b, c, d) -> Instruction(allOperations[opcodesByName[a]!!], b.toInt(), c.toInt(), d.toInt()) }
        }

    return ip to instructions
}


data class UnboundInstruction(val opcode: Int, val input1: Int, val input2: Int, val target: Int)
typealias Registers = IntArray

data class Instruction(val op: Operation, val input1: Int, val input2: Int, val target: Int)

typealias Operation = (r: Registers, a: Int, b: Int) -> Int


fun Registers.execute(operation: Operation, instruction: UnboundInstruction) {
    this[instruction.target] = operation(this, instruction.input1, instruction.input2)
}

fun Registers.execute(instruction: Instruction) {
    this[instruction.target] = instruction.op(this, instruction.input1, instruction.input2)
}

val allOperations = listOf<Operation>(
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

private val opcodesByName = listOf(
    "addr",
    "addi",
    "mulr",
    "muli",
    "banr",
    "bani",
    "borr",
    "bori",
    "setr",
    "seti",
    "gtir",
    "gtri",
    "gtrr",
    "eqir",
    "eqri",
    "eqrr"
)
    .withIndex()
    .associate { (opcode, name) -> name to opcode }