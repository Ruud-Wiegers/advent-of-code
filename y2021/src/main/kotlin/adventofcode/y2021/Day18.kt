package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cartesian

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2021, 18, "Snailfish") {
    override fun solvePartOne(input: String) = input.lineSequence()
        .map { it.toSnailNumber() }
        .reduce(SnailNumber::plus).magnitude()


    override fun solvePartTwo(input: String) = input.lines().cartesian()
        .filter { it.first != it.second }
        .map { it.first.toSnailNumber() + it.second.toSnailNumber() }
        .maxOf(SnailNumber::magnitude)

    private fun String.toSnailNumber(): SnailNumber {
        val digits = mutableListOf<SnailNumber>()
        forEach { ch ->
            when (ch) {
                in '0'..'9' -> digits += V((ch - '0').toLong())
                ']' -> {
                    val r = digits.removeLast()
                    val l = digits.removeLast()
                    digits += P(l, r)
                }
            }
        }
        return digits.first()
    }

    private sealed class SnailNumber {

        var parent: P? = null

        abstract fun magnitude(): Long

        operator fun plus(o: SnailNumber): SnailNumber = P(this, o).reduce()

        fun reduce(): SnailNumber {
            val toExplode =
                traverse(0).filter { it.second == 4 }.map { it.first }.filterIsInstance<P>().firstOrNull()
            if (toExplode != null) {
                val left = traverse(0).map { it.first }.takeWhile { it != toExplode }
                    .filterIsInstance<V>().lastOrNull()
                val right = traverse(0).map { it.first }.dropWhile { it != toExplode }
                    .filterIsInstance<V>().drop(2).firstOrNull()
                toExplode.explode(left, right)
                return reduce()
            }

            val toSplit = traverse(0).map { it.first }.filterIsInstance<V>().firstOrNull { it.v > 9 }
            if (toSplit != null) {
                toSplit.split()
                return reduce()
            }
            return this
        }


        abstract fun traverse(depth: Int): Sequence<Pair<SnailNumber, Int>>
    }

    private class V(var v: Long) : SnailNumber() {


        override fun magnitude() = v

        fun split() {

            val new = P(V(v / 2), V((v + 1) / 2))
            new.parent = parent

            if (parent!!.left == this) parent!!.left = new
            else parent!!.right = new
        }

        override fun traverse(depth: Int) = sequenceOf(Pair(this, depth))

        override fun toString() = v.toString()
    }

    private class P(var left: SnailNumber, var right: SnailNumber) : SnailNumber() {

        init {
            left.parent = this
            right.parent = this
        }

        override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()


        override fun traverse(depth: Int) =
            sequenceOf(Pair(this, depth)) + left.traverse(depth + 1) + right.traverse(depth + 1)


        fun explode(left: V?, right: V?) {
            val new = V(0)
            new.parent = parent

            if (parent!!.left == this) parent!!.left = new
            else parent!!.right = new

            left?.let { it.v += (this.left as V).v }
            right?.let { it.v += (this.right as V).v }

        }

        override fun toString() = "[$left,$right]"
    }
}

