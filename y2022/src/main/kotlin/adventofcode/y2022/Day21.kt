package adventofcode.y2022

import adventofcode.AdventSolution


object Day21 : AdventSolution(2022, 21, "Monkey Math") {

    override fun solvePartOne(input: String): Long {

        val instructions = mutableMap(input)

        return instructions.getValue("root")()
    }

    override fun solvePartTwo(input: String): Long {

        val instructions = mutableMap(input)

        val target = instructions.getValue("hghd")()

        return generateSequence(1L to 10_000_000_000_000) { (low, high) ->
            val mid = (low + high) / 2
            instructions["humn"] = { mid }

            val new = instructions.getValue("zhfp")()

            if (new > target) mid to high else low to mid
        }
            .first { it.first + 1 >= it.second }
            .second


    }

    private fun mutableMap(input: String): MutableMap<String, () -> Long> {
        val instructions = mutableMapOf<String, () -> Long>()

        input.lineSequence().forEach {
            val (name, instr) = it.split(": ")

            val constant = instr.toLongOrNull()

            instructions[name] = if (constant != null) {
                { constant }
            } else {

                val (lhs, op, rhs) = instr.split(" ")
                val operator: (Long, Long) -> Long = when (op) {
                    "+" -> Long::plus
                    "-" -> Long::minus
                    "*" -> Long::times
                    "/" -> Long::div
                    else -> throw IllegalArgumentException(op)
                }

                { operator(instructions.getValue(lhs)(), instructions.getValue(rhs)()) }
            }
        }
        return instructions
    }


}