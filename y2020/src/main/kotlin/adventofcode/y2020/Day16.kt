package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day16.solve()

object Day16 : AdventSolution(2020, 16, "Ticket Translation")
{
    override fun solvePartOne(input: String): Int
    {
        val p = parse(input)
        return p.otherTickets.flatten().filter { x -> p.fields.none { f -> x in f } }.sum()
    }

    override fun solvePartTwo(input: String): Any
    {
        val p = parse(input).filterInvalidTickets()

        val fieldMapping = p.deducePropertyMapping()
        return fieldMapping
            .filterKeys { it.startsWith("departure") }
            .values.map { p.yourTicket[it] }
            .fold(1L, Long::times)
    }

    private fun Problem.deducePropertyMapping(): Map<String, Int>
    {
        val columns = otherTickets.plusElement(yourTicket).transpose()

        val candidateFields = columns.map { xs ->
            fields.filter { field -> xs.all(field::contains) }.toMutableSet()
        }

        val result = mutableMapOf<Field, Int>()

        while (candidateFields.any { it.isNotEmpty() })
        {
            val c = candidateFields.indexOfFirst { it.size == 1 }
            if (c == -1) throw IllegalStateException()
            val element = candidateFields[c].first()
            result[element] = c
            candidateFields.forEach { it -= element }
        }

        return result.mapKeys { it.key.name }
    }
}

private fun <T> List<List<T>>.transpose(): List<List<T>> = first().indices.map { index -> map { it[index] } }

private fun Problem.filterInvalidTickets(): Problem
{
    val filtered = otherTickets.filter { xs -> xs.all { x -> fields.any { r -> x in r } } }

    return copy(otherTickets = filtered)
}

data class Problem(val fields: List<Field>, val yourTicket: List<Int>, val otherTickets: List<List<Int>>)

fun parse(input: String): Problem
{
    fun parseToField(input: String): Field
    {
        val propRegex = """([\s\w]+): (\d+)-(\d+) or (\d+)-(\d+)""".toRegex()
        val (name, l1, h1, l2, h2) = propRegex.matchEntire(input)!!.destructured
        return Field(name, l1.toInt()..h1.toInt(), l2.toInt()..h2.toInt())
    }

    fun parseToTicket(input: String) = input.split(',').map { it.toInt() }

    val (fields, yourTicket, otherTickets) = input.split("\n\n")

    return Problem(
        fields.lines().map(::parseToField),
        parseToTicket(yourTicket.lines().last()),
        otherTickets.lines().drop(1).map(::parseToTicket))
}

data class Field(val name: String, val r1: IntRange, val r2: IntRange)
{
    operator fun contains(i: Int) = i in r1 || i in r2
}
