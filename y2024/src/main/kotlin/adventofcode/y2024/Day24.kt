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

    override fun solvePartTwo(input: String): String {
        //ASSUMPTION: this is a full adder with no further shenanigans
        //NOTE: this is not a general solution, more of a notebook for hand-solving my input
        //hiding the concrete input, but not the 'pattern' of wire swaps

        val (initialWires, initialGates) = parseInput(input)

        val wires = initialWires.toMutableMap()
        val gates = initialGates.toMutableList()

        val inputPairs = wires.keys.sorted().partition { it.startsWith("x") }.let { (a, b) -> a.zip(b) }

        //xi XOR yi -> xor1[i]
        val xor1 = inputPairs.map { (x, y) ->
            gates.first { (it.left == x || it.right == x) && (it.left == y || it.right == y) && it.type == Gate.XOR }.out
        }

        //ci AND yi -> and1[i]
        val and1 = inputPairs.map { (x, y) ->
            gates.first { (it.left == x || it.right == x) && (it.left == y || it.right == y) && it.type == Gate.AND }.out
        }

        val outputs = gates.filter { it.out.startsWith("z") && it.type == Gate.XOR }.sortedBy { it.out }

        fun find(input: String, type: Gate) = gates.first { (it.left == input || it.right == input) && it.type == type }

        val miswiredOutputs = (0..44).map { "z"+it.toString().padStart(2,'0') }.filter { it !in outputs.map { it.out } }


        val miswiredOutputPairs = miswiredOutputs.map { it.drop(1).toInt() }.map { find(xor1[it], Gate.XOR).out }


        var miswired = (miswiredOutputs+miswiredOutputPairs).toMutableList()

        for (i in 1..44) {

                val out1 = and1[i]
                val out2 = try {find(out1, Gate.OR).out} catch (e: NoSuchElementException) {continue}
                val out3 = try {find(out2, Gate.AND).out} catch (e: NoSuchElementException) {continue}
                val orGate = try {find(out3, Gate.OR)} catch (e: NoSuchElementException) {continue}

                val input1 = and1[i + 1]
                val next = try {find(input1, Gate.OR) } catch (e: NoSuchElementException) {null}

                if (next == null) {
                    if ( input1 in miswiredOutputs) continue
                    miswired += input1
                    miswired += listOf(orGate.left, orGate.right).filter { it != out3 }
                }

        }
        return miswired.sorted().joinToString(",")
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