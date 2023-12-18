package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day19.solve()
}

object Day19 : AdventSolution(2023, 19, "Aplenty") {

    override fun solvePartOne(input: String): Long {
        val (workflows, parts) = parse(input)

        val flows = workflows.associateBy { it.name }


        fun accepted(part: Part): Boolean {
            var current: Target = Target.Flow("in")

            while (current is Target.Flow) {
                current = flows[current.name]!!.eval(part)
            }

            return current == Target.Accept
        }

        val result = parts.filter { accepted(it) }.sumOf { it.x + it.m + it.a + it.s }

        return result.toLong()
    }

    override fun solvePartTwo(input: String): Long {
        val (workflows) = parse(input)

        val boundaries = workflows.flatMap { it.steps }.groupBy { it.property }
            .mapValues {

                val cuts = it.value.map { if (it.lessThan) it.v else it.v + 1 }.toSet().sorted()

                listOf(1) + cuts + 4001
            }


        val groups =
            boundaries.mapValues { it.value.zipWithNext { start, endExclusive -> start to endExclusive - start } }






        val flows = workflows.associateBy { it.name }

        fun accepted(part: Part): Boolean {
            var current: Target = Target.Flow("in")

            while (current is Target.Flow) {
                current = flows[current.name]!!.eval(part)
            }

            return current == Target.Accept
        }



        var result = 0L

        groups.forEach{ println(it.value.size) }

        for ((x, xLen) in groups.getValue('x')) {
            println(x)
            for ((m, mLen) in groups.getValue('m')) {
                for ((a, aLen) in groups.getValue('a')) {
                    for ((s, sLen) in groups.getValue('s')) {
                        if(accepted(Part(x, m, a, s))) {
                            result += xLen.toLong() * mLen.toLong() * aLen.toLong() * sLen.toLong()
                        }
                    }
                }
            }
        }
        return result

    }

}


private fun parse(input: String): Pair<List<Workflow>, List<Part>> {
    val (workflows, parts) = input.split("\n\n")

    return workflows.lines().map { parseWorkflow(it) } to parts.lines().map { parsePart(it) }
}

private fun parseWorkflow(input: String): Workflow {
    val name = input.substringBefore("{")
    val flows = input.substringAfter("{").dropLast(1).split(",")
    val default = flows.last().let(::parseTarget)

    val comparisons = flows.dropLast(1).map {
        val property = it.first()
        val lessThan = it[1] == '<'
        val amount = it.substring(2).substringBefore(":").toInt()
        val target = it.substringAfter(":").let(::parseTarget)
        Comparison(property, lessThan, amount, target)
    }

    return Workflow(name, comparisons, default)
}

private fun parseTarget(input: String) = when (input) {
    "A" -> Target.Accept
    "R" -> Target.Reject
    else -> Target.Flow(input)
}

private data class Workflow(val name: String, val steps: List<Comparison>, val default: Target) {
    fun eval(part: Part): Target = steps.firstNotNullOfOrNull { it.eval(part) } ?: default
}

private data class Comparison(val property: Char, val lessThan: Boolean, val v: Int, val target: Target) {

    fun eval(part: Part): Target? {
        val p = when (property) {
            'x' -> part.x
            'm' -> part.m
            'a' -> part.a
            's' -> part.s
            else -> error("")
        }

        return if (lessThan && p < v) target
        else if (!lessThan && p > v) target
        else null
    }
}


private sealed class Target {
    data object Accept : Target()
    data object Reject : Target()
    data class Flow(val name: String) : Target()
}

private fun parsePart(input: String): Part {
    val (x, m, a, s) = """(\d+)""".toRegex().findAll(input).map { it.value.toInt() }.toList()
    return Part(x, m, a, s)
}

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int)