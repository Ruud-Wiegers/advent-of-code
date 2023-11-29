package adventofcode.y2020

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.SimpleParser

fun main() = Day14.solve()

object Day14 : AdventSolution(2020, 14, "Docking Data")
{

    override fun solvePartOne(input: String): Long
    {
        val mem = mutableMapOf<Long, Long>()
        lateinit var mask: String

        fun applyMask(i: Long): Long
        {
            val str = i.toString(2).padStart(36, '0')

            return mask.zip(str) { m, ch -> if (m == 'X') ch else m }.toCharArray().let(::String).toLong(2)
        }

        val parser = SimpleParser<() -> Unit>().apply {
            rule("""mem\[(\d+)\] = (\d+)""") { (adr, v) -> { mem[adr.toLong()] = applyMask(v.toLong()) } }
            rule("""mask = (.+)""") { (s) -> { mask = s } }
        }

        input.lineSequence().mapNotNull { parser.parse(it) }.forEach { it() }

        return mem.values.sum()
    }

    override fun solvePartTwo(input: String): Any
    {
        val mem = mutableMapOf<Long, Long>()
        lateinit var mask: String

        fun applyMask(adr: Long): LongArray
        {
            val str = adr.toString(2).padStart(36, '0')

            var masked = LongArray(1)

            mask.zip(str) { m, ch ->
                for (i in masked.indices) masked[i] *= 2L

                when
                {
                    m == 'X'              -> masked += masked.map { it + 1 }
                    m == '1' || ch == '1' -> for (i in masked.indices) masked[i]++
                }
            }
            return masked
        }

        val parser = SimpleParser<() -> Unit>().apply {
            rule("""mem\[(\d+)\] = (\d+)""") { (adr, v) -> { applyMask(adr.toLong()).forEach { a -> mem[a] = v.toLong() } } }
            rule("""mask = (.+)""") { (s) -> { mask = s } }
        }

        input.lineSequence().mapNotNull(parser::parse).forEach { it() }

        return mem.values.sum()
    }
}
