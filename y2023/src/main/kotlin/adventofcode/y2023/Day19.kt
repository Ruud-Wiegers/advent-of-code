package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day19.solve()
}

object Day19 : AdventSolution(2023, 19, "Aplenty") {

    override fun solvePartOne(input: String): Int {
        val (workflows, parts) = parse(input)

        val flows = workflows.associateBy { it.name }

        return parts.filter { it.acceptedBy(flows) }.sumOf(Part::value)
    }

    override fun solvePartTwo(input: String): Long {
        val workflows = parse(input).first.associateBy { it.name }

        val initial = PartRange(Part("xmas".map { it to 1 }.toMap()), Part("xmas".map { it to 4001 }.toMap()))

        val incomplete = mutableListOf<Pair<PartRange, Evaluation>>(initial to Evaluation.DecideByWorkflow("in"))
        val accepted = mutableListOf<PartRange>()

        while (incomplete.isNotEmpty()) {
            val (currentRange, currentTarget) = incomplete.removeLast()

            when (currentTarget) {
                is Evaluation.DecideByWorkflow -> {
                    incomplete += workflows.getValue(currentTarget.name).evaluatePartRange(currentRange)
                }
                Evaluation.Accepted -> accepted += currentRange
                Evaluation.Rejected -> continue
            }
        }

        return accepted.sumOf(PartRange::countPartsWithinRange)
    }
}


private fun parse(input: String): Pair<List<Workflow>, List<Part>> {

    fun parsePart(input: String): Part {
        val values = """(\d+)""".toRegex().findAll(input).map { it.value.toInt() }.toList()
        return "xmas".asIterable().zip(values).toMap().let (::Part)
    }

    fun parseTarget(input: String) = when (input) {
        "A" -> Evaluation.Accepted
        "R" -> Evaluation.Rejected
        else -> Evaluation.DecideByWorkflow(input)
    }

    fun parseWorkflow(input: String): Workflow {
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


    val (workflows, parts) = input.split("\n\n")

    return workflows.lines().map { parseWorkflow(it) } to parts.lines().map { parsePart(it) }
}


private data class Workflow(val name: String, val steps: List<Comparison>, val default: Evaluation) {
    fun evaluatePartRange(part: Part): Evaluation = steps.firstNotNullOfOrNull { it.eval(part) } ?: default

    fun evaluatePartRange(partRange: PartRange): List<Pair<PartRange, Evaluation>> {
        val resolved = mutableListOf<Pair<PartRange, Evaluation>>()
        val remainder = steps.fold(partRange) { range, comparison ->
            val (res, rem) = comparison.eval(range)
            resolved += res
            rem
        }

        return resolved + (remainder to default)
    }
}

private data class Comparison(val property: Char, val lessThan: Boolean, val v: Int, val evaluation: Evaluation) {

    fun eval(part: Part): Evaluation? {

        return if (lessThan && part[property] < v) evaluation
        else if (!lessThan && part[property] > v) evaluation
        else null
    }

    fun eval(range: PartRange): Pair<Pair<PartRange, Evaluation>, PartRange> =
        if (lessThan) {
            val (low, high) = range.partition(property, v)
            Pair(low to evaluation, high)
        } else {
            val (low, high) = range.partition(property, v + 1)
            Pair(high to evaluation, low)
        }

}

private sealed class Evaluation {
    data object Accepted : Evaluation()
    data object Rejected : Evaluation()
    data class DecideByWorkflow(val name: String) : Evaluation()
}


private data class Part(private val props:Map<Char,Int>) {
    fun copyWith(property: Char, value: Int) = Part(props + (property to value))

    operator fun get(property: Char) = props.getValue(property)

    fun value() = props.values.sum()
    fun area() = props.values.fold(1L,Long::times)
    operator fun minus(o:Part) = Part(o.props.mapValues { (p,v)-> v - this[p] })


    fun acceptedBy(workflows: Map<String, Workflow>): Boolean = generateSequence<Evaluation>(Evaluation.DecideByWorkflow("in")) {
        (it as? Evaluation.DecideByWorkflow)?.name?.let(workflows::get)?.evaluatePartRange(this)
    }.last() == Evaluation.Accepted

}

private data class PartRange(val start: Part, val endExclusive: Part) {

    fun partition(property: Char, startOfHigh: Int): Pair<PartRange, PartRange> {
        val lower = copy(endExclusive = endExclusive.copyWith(property, startOfHigh))
        val higher = copy(start = start.copyWith(property, startOfHigh))
        return lower to higher
    }

    fun countPartsWithinRange() = (endExclusive - start).area()
}