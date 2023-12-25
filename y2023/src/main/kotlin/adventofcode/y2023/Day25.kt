package adventofcode.y2023

import adventofcode.io.AdventSolution

fun main() {
    Day25.solve()
}

object Day25 : AdventSolution(2023, 25, "Snowverload") {

    override fun solvePartOne(input: String): Any {
        val weightedGraph: WeightedGraph = parse(input)
            .mapValues { Desc(1, it.value.associateWith { 1 }.toMutableMap()) }
            .toMutableMap()

        while (weightedGraph.size > 2) {
            val (s, t) = weightedGraph.findMinCut()

            val cut = weightedGraph.getValue(t).edgeWeights.values.sum()
            if (cut == 3) {
                return weightedGraph.filterKeys { it != t }.values.sumOf { it.vertexCount } * weightedGraph.getValue(t).vertexCount
            }
            weightedGraph.merge(s, t)
        }

        error("No solution")
    }

    override fun solvePartTwo(input: String) = "Free!"

}

private fun parse(input: String): Map<Int, List<Int>> {
    val graph = input.lines().flatMap {
        val (s, es) = it.split(": ")

        es.split(" ").flatMap { e -> listOf(s to e, e to s) }
    }.groupBy({ it.first }, { it.second })

    val keys = graph.keys.withIndex().associate { it.value to it.index }

    return graph.mapKeys { keys.getValue(it.key) }.mapValues { it.value.map { keys.getValue(it) } }

}

private data class Desc(var vertexCount: Int, val edgeWeights: MutableMap<Int, Int>)
private typealias WeightedGraph = MutableMap<Int, Desc>

private fun WeightedGraph.findMinCut(): List<Int> {
    val a = mutableSetOf(keys.first())
    val rem = (keys - a).toMutableSet()

    while (rem.size > 2) {

        val next = rem.maxBy { v ->
            getValue(v).edgeWeights.entries.sumOf { if (it.key in a) it.value else 0 }
        }
        a += next
        rem -= next
    }

    val next = rem.maxBy { v ->
        getValue(v).edgeWeights.entries.sumOf { if (it.key in a) it.value else 0 }
    }


    return listOf(next, (rem - next).first())
}

private fun WeightedGraph.merge(s: Int, t: Int) {
    val tDesc = remove(t)!!
    val stDesc = getValue(s)

    tDesc.edgeWeights.forEach { (k, v) -> stDesc.edgeWeights.merge(k, v, Int::plus) }
    stDesc.edgeWeights -= s
    stDesc.edgeWeights -= t
    stDesc.vertexCount += tDesc.vertexCount

    for ((_, w) in this.values) {
        w.remove(t)?.let { w.merge(s, it, Int::plus) }
    }
}
