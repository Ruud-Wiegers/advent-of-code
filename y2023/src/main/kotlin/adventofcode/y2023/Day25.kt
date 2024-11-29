package adventofcode.y2023

import adventofcode.io.AdventSolution

fun main() {
    Day25.solve()
}

object Day25 : AdventSolution(2023, 25, "Snowverload") {

    override fun solvePartOne(input: String): Any {
        val weightedGraph: WeightedGraph = parse(input)
            .mapValues { Desc(1, it.value.associateWith { 1 }.toMutableMap()) }
            .entries.sortedBy { it.key }.map { it.value }.toTypedArray()

        repeat(weightedGraph.size - 2) {
            val (s, t) = weightedGraph.findMinCut()


            val cut = weightedGraph[t].edgeWeights.values.sum()
            if (cut == 3) {
                val wt = weightedGraph[t].vertexCount
                return (weightedGraph.sumOf { it.vertexCount } - wt) * wt
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

private typealias WeightedGraph = Array<Desc>

private fun WeightedGraph.findMinCut(): List<Int> {

    val last = indexOfLast { it.vertexCount > 0 }
    val grouped = BooleanArray(last + 1)
    grouped[0] = true

    val weightsFromA = IntArray(last + 1)

    this[0].edgeWeights.forEach { weightsFromA[it.key] += it.value }

    repeat(last - 2) {


        val next = weightsFromA.indexOf(weightsFromA.max())
        grouped[next] = true
        weightsFromA[next] = 0
        this[next].edgeWeights.forEach { if (!grouped[it.key]) weightsFromA[it.key] += it.value }
    }

    val c1 = weightsFromA.indexOfFirst { it > 0 }
    val c2 = weightsFromA.indexOfLast { it > 0 }
    return listOf(c1, c2).sortedByDescending { weightsFromA[it] }
}


//merge s and t, move the empty t to the end of the array
private fun WeightedGraph.merge(s: Int, t: Int) {
    val tDesc = get(t)
    val stDesc = get(s)


    tDesc.edgeWeights.forEach { (k, v) -> stDesc.edgeWeights.merge(k, v, Int::plus) }
    stDesc.edgeWeights -= s
    stDesc.edgeWeights -= t
    stDesc.vertexCount += tDesc.vertexCount

    val swap = maxOf(t, indexOfLast { it.vertexCount > 0 })

    for ((_, w) in this) {
        w.remove(t)?.let { w.merge(s, it, Int::plus) }
        w.remove(swap)?.let { w[t] = it }
    }

    this[t] = this[swap]
    this[swap] = Desc(0, mutableMapOf())

}
