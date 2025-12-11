package adventofcode.y2025

import adventofcode.io.AdventSolution

fun main() {
    Day11.solve()
}

object Day11 : AdventSolution(2025, 11, "Reactor") {

    override fun solvePartOne(input: String): Long {
        val fullTree = parse(input)
        return countPaths(fullTree, "you")["out"]!!
    }

    override fun solvePartTwo(input: String): Long {
        val fullTree = parse(input)

        val fromSvr = countPaths(fullTree, "svr")
        val fromDac = countPaths(fullTree, "dac")
        val fromFft = countPaths(fullTree, "fft")

        val svrToDac = fromSvr["dac"] ?: 0L
        val dacToFft = fromDac["fft"] ?: 0L
        val fftToOut = fromFft["out"] ?: 0L
        val path1 = svrToDac * dacToFft * fftToOut

        val svrToFft = fromSvr["fft"] ?: 0L
        val fftToDac = fromFft["dac"] ?: 0L
        val dacToOut = fromDac["out"] ?: 0L
        val path2 = svrToFft * fftToDac * dacToOut

        return path1 + path2
    }

}

private fun parse(input: String): Map<String, List<String>> = input.lines().associate { line ->
    val (node, links) = line.split(": ")
    node to links.split(" ")
}


private fun countPaths(fullTree: Map<String, List<String>>, start: String): Map<String, Long> {
    val forwards = fullTree.pruneUnreachable(start)
    val backwards = forwards.invert()

    val paths = mutableMapOf(start to 1L)
    val open = forwards[start].orEmpty().toMutableSet()

    while (open.isNotEmpty()) {
        val candidate = open.first {
            backwards[it].orEmpty().all { feed -> feed in paths }
        }
        open.remove(candidate)
        paths[candidate] = backwards[candidate].orEmpty().sumOf { paths.getValue(it) }
        open += forwards[candidate].orEmpty()
    }

    return paths
}

private fun Map<String, List<String>>.pruneUnreachable(start: String): Map<String, List<String>> {
    var open = setOf(start)
    val visited = mutableSetOf(start)

    while (open.isNotEmpty()) {
        visited += open
        open = open.flatMap { this[it].orEmpty() }.toSet()
    }

    return this
        .filterKeys { it in visited }
        .mapValues { it.value.filter { it in visited } }
}


private fun Map<String, List<String>>.invert() = entries
    .flatMap { (node, links) -> links.map { it to node } }
    .groupBy({ it.first }, { it.second })
