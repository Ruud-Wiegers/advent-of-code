package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day23.solve()
}

object Day23 : AdventSolution(2024, 23, "LAN Party") {

    override fun solvePartOne(input: String): Int =
        clustering(input).elementAt(2).count { it.any { c -> c.startsWith("t") } }

    override fun solvePartTwo(input: String): String =
        clustering(input).last().single().joinToString(",")

    private fun clustering(input: String): Sequence<List<List<String>>> {
        val connections = input.parseInput()

        fun next(groups: List<List<String>>) = groups.flatMap { old ->
            connections.keys
                .filter { candidate ->
                    old.last() < candidate && old.all { oc -> oc in connections[candidate].orEmpty() }
                }
                .map { old + it }
        }.takeIf { it.isNotEmpty() }

        val initial = connections.keys.map { listOf(it) }
        return generateSequence(initial, ::next)
    }

    fun String.parseInput() = lineSequence().flatMap {
        val list = it.split("-")
        listOf(list, list.reversed())
    }.groupBy({ it[0] }, { it[1] }).mapValues { it.value.toSet() }
}