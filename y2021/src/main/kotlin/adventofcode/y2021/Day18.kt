package adventofcode.y2021

import adventofcode.AdventSolution

object Day18 : AdventSolution(2021, 18, "Snailfish") {
    override fun solvePartOne(input: String): Int {
        val numbers = input.lines().map(::toSnailNumber)
        return numbers.reduce(SnailfishNumber::plus).magnitude()
    }

    override fun solvePartTwo(input: String): Int {
        val numbers = input.lines().map(::toSnailNumber)
        return numbers.flatMap { a -> numbers.filterNot(a::equals).map(a::plus) }.maxOf { it.magnitude() }
    }

    private fun toSnailNumber(s: String) = SnailfishNumber(buildList {
        var depth = 0
        s.forEach { ch ->
            when (ch) {
                '[' -> depth++
                ']' -> depth--
                in '0'..'9' -> add(Element(ch - '0', depth))
            }
        }
    })

    private data class SnailfishNumber(val values: List<Element>) {
        operator fun plus(o: SnailfishNumber) = SnailfishNumber((values + o.values).map { it.addDepth(1) }).simplify()

        fun simplify(): SnailfishNumber = explode()?.simplify() ?: split()?.simplify() ?: this

        private fun explode(): SnailfishNumber? {
            val i = values.indexOfFirst { it.depth == 5 }
            return if (i < 0) null else SnailfishNumber(values.toMutableList().also { new ->
                val (left, _) = new.removeAt(i)
                val (right, _) = new.removeAt(i)
                new.add(i, Element(0, 4))
                if (i - 1 in new.indices) new[i - 1] = new[i - 1].addValue(left)
                if (i + 1 in new.indices) new[i + 1] = new[i + 1].addValue(right)
            })
        }

        private fun split(): SnailfishNumber? {
            val i = values.indexOfFirst { it.value > 9 }
            return if (i < 0) null
            else SnailfishNumber(values.toMutableList().also { new ->
                val (oldV, oldD) = new.removeAt(i)
                new.add(i, Element(oldV / 2, oldD + 1))
                new.add(i + 1, Element((oldV + 1) / 2, oldD + 1))
            })
        }

        fun magnitude(): Int {
            val stack = mutableListOf<Element>()

            tailrec fun tryAdd(r: Element) {
                if ((stack.isEmpty() || stack.last().depth != r.depth)) stack.add(r)
                else tryAdd(stack.removeLast().magnitudeWith(r))
            }

            values.forEach(::tryAdd)
            return stack.first().value
        }
    }

    private data class Element(val value: Int, val depth: Int) {
        fun addDepth(a: Int) = copy(depth = depth + a)
        fun addValue(a: Int) = copy(value = value + a)
        fun magnitudeWith(o: Element) = Element(value * 3 + o.value * 2, depth - 1)
    }
}
