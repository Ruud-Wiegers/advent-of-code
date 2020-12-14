package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.SimpleParser

fun main() = Day14.solve()

object Day14 : AdventSolution(2020, 14, "Docking Data")
{

    override fun solvePartOne(input: String): Long
    {
        val mem = mutableMapOf<Int, Long>()
        var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        val parser = SimpleParser<() -> Unit>()

        fun applyMask(i: Long): Long
        {
            val str = i.toString(2).padStart(36, '0')

            val masked = mask.zip(str) { m, ch -> if (m == 'X') ch else m }.toCharArray().let(::String)

            return masked.toLong(2)
        }

        parser.rule("""mem\[(\d+)\] = (\d+)""") { (adr, v) ->
            { mem[adr.toInt()] = applyMask(v.toLong()) }
        }
        parser.rule("""mask = (.+)""") { (s) -> { mask = s } }

        input.lineSequence().mapNotNull { parser.parse(it) }.forEach { it() }

        return mem.values.sum()
    }

    override fun solvePartTwo(input: String): Any
    {
        val mem = mutableMapOf<Long, Long>()
        var mask = "000000000000000000000000000000000000"
        val parser = SimpleParser<() -> Unit>()

        fun applyMask(adr: Long): List<Long>
        {
            val str = adr.toString(2).padStart(36, '0')

            var masked = listOf("")

            mask.zip(str) { m, ch ->
                masked = when (m)
                {
                    '0' -> masked.map { it + ch }
                    '1' -> masked.map { it + '1' }
                    'X' -> masked.map { it + '0' } + masked.map { it + '1' }
                    else -> masked

                }
            }
            return masked.map { it.toLong(2) }
        }

        parser.rule("""mem\[(\d+)\] = (\d+)""") { (adr, v) ->
            { applyMask(adr.toLong()).forEach { a -> mem[a] = v.toLong() } }
        }
        parser.rule("""mask = (.+)""") { (s) -> { mask = s } }

        input.lineSequence().mapNotNull { parser.parse(it) }.forEach { it() }

        return mem.values.sum()
    }
}
