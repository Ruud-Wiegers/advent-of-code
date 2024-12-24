package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day24.solve()
}

object Day24 : AdventSolution(2024, 24, "Crossed Wires") {

    override fun solvePartOne(input: String): Long {
        val (initialWires, initialGates) = parseInput(input)

        val wires = initialWires.toMutableMap()
        val gates = initialGates.toMutableList()

        while (gates.isNotEmpty()) {
            val gate = gates.first { it.left in wires.keys && it.right in wires.keys }

            wires[gate.out] = when (gate.type) {
                Gate.AND -> wires.getValue(gate.left) && wires.getValue(gate.right)
                Gate.OR -> wires.getValue(gate.left) || wires.getValue(gate.right)
                Gate.XOR -> wires.getValue(gate.left) xor wires.getValue(gate.right)
            }

            gates.remove(gate)
        }


        val toSortedMap = wires.filterKeys { it.startsWith("z") }.toSortedMap()

        return toSortedMap.values.reversed()
            .joinToString("") { if (it) "1" else "0" }
            .toLong(2)

    }

    override fun solvePartTwo(input: String): Int {
        val (initial, gates) = parseInput(input)

        return 0
    }

    private fun parseInput(input: String): Pair<Map<String, Boolean>, List<GateConnection>> {

        val (wireStr, gateStr) = input.split("\n\n")

        val initial = wireStr.lines().associate { it.split(": ").let { it[0] to (it[1] == "1") } }


        val regex = """(...) (AND|OR|XOR) (...) -> (...)""".toRegex()

        val connections = gateStr.lines().map { line ->
            val (l, g, r, o) = regex.matchEntire(line)?.destructured!!
            GateConnection(l, Gate.v(g), r, o)
        }

        return initial to connections

    }
}


private enum class Gate {
    AND, OR, XOR;

    companion object {
        fun v(input: String): Gate = when (input) {
            "AND" -> Gate.AND
            "OR" -> Gate.OR
            "XOR" -> Gate.XOR
            else -> error("Unknown gate $input")
        }
    }
}

private data class GateConnection(val left: String, val type: Gate, val right: String, val out: String)