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
        val workflows = parse(input).first.associateBy { it.name }

        val initial = PartRange(Part(1, 1, 1, 1), Part(4001, 4001, 4001, 4001))


        val incomplete = mutableListOf<Pair<PartRange, Target>>(initial to Target.Flow("in"))
        val accepted = mutableListOf<PartRange>()

        while (incomplete.isNotEmpty()) {
            val (currentRange, currentTarget) = incomplete.removeLast()

            when (currentTarget) {
                Target.Accept -> accepted += currentRange
                Target.Reject -> continue
                is Target.Flow -> {
                    incomplete += workflows.getValue(currentTarget.name).eval(currentRange)
                }
            }
        }

        return accepted.sumOf(PartRange::count)

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

private fun parsePart(input: String): Part {
    val (x, m, a, s) = """(\d+)""".toRegex().findAll(input).map { it.value.toInt() }.toList()
    return Part(x, m, a, s)
}


private data class Workflow(val name: String, val steps: List<Comparison>, val default: Target) {
    fun eval(part: Part): Target = steps.firstNotNullOfOrNull { it.eval(part) } ?: default

    fun eval(partRange: PartRange): List<Pair<PartRange, Target>> {

        val resolved = mutableListOf<Pair<PartRange, Target>>()
        val remainder = steps.fold(partRange) { range, comparison ->
            val (res, rem) = comparison.eval(range)
            resolved += res
            rem
        }

        return resolved + (remainder to default)
    }
}

private data class Comparison(val property: Char, val lessThan: Boolean, val v: Int, val target: Target) {

    fun eval(part: Part): Target? {
        val p = select(part)

        return if (lessThan && p < v) target
        else if (!lessThan && p > v) target
        else null
    }

    fun eval(range: PartRange): Pair<Pair<PartRange, Target>, PartRange> =
        if (lessThan) {
            val (low, high) = range.partition(property, v)
            Pair(low to target, high)
        } else {
            val (low, high) = range.partition(property, v + 1)
            Pair(high to target, low)
        }

    private fun select(part: Part): Int = when (property) {
            'x' -> part.x
            'm' -> part.m
            'a' -> part.a
            's' -> part.s
            else -> error("")
        }
}

private sealed class Target {
    data object Accept : Target()
    data object Reject : Target()
    data class Flow(val name: String) : Target()
}


private data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun copyWith(property: Char, value: Int) = when (property) {
        'x' -> copy(x = value)
        'm' -> copy(m = value)
        'a' -> copy(a = value)
        's' -> copy(s = value)
        else -> error("unknown property")
    }

    operator fun minus(o: Part) = Part(o.x - x, o.m - m, o.a - a, o.s - s)
}

private data class PartRange(val start: Part, val endExclusive: Part) {

    fun partition(property: Char, startOfHigh: Int): Pair<PartRange, PartRange> {
        val lower = copy(endExclusive = endExclusive.copyWith(property, startOfHigh))
        val higher = copy(start = start.copyWith(property, startOfHigh))

        return lower to higher
    }

    fun count() = (endExclusive - start).let { (x, m, a, s) -> 1L * x * m * a * s }
}