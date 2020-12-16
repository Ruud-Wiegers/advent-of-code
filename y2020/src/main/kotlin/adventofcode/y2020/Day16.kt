package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day16.solve()

object Day16 : AdventSolution(2020, 16, "Ticket Translation")
{
    override fun solvePartOne(input: String): Int
    {
        val (fields, _, otherTickets) = parse(input)

        return otherTickets.flatten()
            .filter { value -> fields.none { field -> value in field } }
            .sum()
    }

    override fun solvePartTwo(input: String): Any
    {
        val p = parse(input).filterInvalidTickets()

        return p.deduceFieldNameMapping()
            .filterKeys { it.startsWith("departure") }
            .values
            .map { p.yourTicket[it] }
            .fold(1L, Long::times)
    }
}

private fun ProblemData.filterInvalidTickets() = copy(otherTickets = otherTickets.filter { xs ->
    xs.all { x -> fields.any { range -> x in range } }
})

private fun ProblemData.deduceFieldNameMapping(): Map<String, Int>
{
    val dataset = otherTickets.plusElement(yourTicket).transpose()

    val candidateNames = dataset.map { column ->
        fields
            .filter { field -> column.all(field::contains) }
            .map(Field::name)
            .toMutableSet()
    }

    return buildMap {

        while (candidateNames.any { it.isNotEmpty() })
        {
            val c = candidateNames.indexOfFirst { it.size == 1 }
            val name = candidateNames[c].single()
            this[name] = c
            for (field in candidateNames) field -= name
        }

    }
}

private fun <T> List<List<T>>.transpose(): List<List<T>> = first().indices.map { index -> map { it[index] } }

private fun parse(input: String): ProblemData
{
    fun parseToField(input: String): Field
    {
        val propRegex = """([\s\w]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
        val (name, l1, h1, l2, h2) = propRegex.matchEntire(input)!!.destructured
        return Field(name, l1.toInt()..h1.toInt(), l2.toInt()..h2.toInt())
    }

    fun parseToTicket(input: String) = input.split(',').map { it.toInt() }

    val (fields, yourTicket, otherTickets) = input.split("\n\n")

    return ProblemData(
        fields = fields.lines().map(::parseToField),
        yourTicket = parseToTicket(yourTicket.lines().last()),
        otherTickets = otherTickets.lines().drop(1).map(::parseToTicket))
}

private data class ProblemData(val fields: List<Field>, val yourTicket: List<Int>, val otherTickets: List<List<Int>>)

private data class Field(val name: String, val r1: IntRange, val r2: IntRange)
{
    operator fun contains(i: Int) = i in r1 || i in r2
}