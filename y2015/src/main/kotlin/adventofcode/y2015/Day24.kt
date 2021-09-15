package adventofcode.y2015

import adventofcode.AdventSolution

object Day24 : AdventSolution(2015, 24, "It Hangs in the Balance")
{
    override fun solvePartOne(input: String) = input.lines().map(String::toInt).let { solve(it, 3) }

    override fun solvePartTwo(input: String) = input.lines().map(String::toInt).let { solve(it, 4) }

    private fun solve(presents: List<Int>, groupCount: Int): Long?
    {
        val target = presents.sum() / groupCount

        //meet-in-the-middle style solution
        val a: Map<Int, Group> = powerSetByWeight(presents.filterIndexed { i, _ -> i % 2 == 0 }, target)
        val b: Map<Int, Group> = powerSetByWeight(presents.filterIndexed { i, _ -> i % 2 != 0 }, target)

        return (0..target)
            .minOfOrNull { i ->
                (a[i] ?: Group.max).mergeWith(b[target - i] ?: Group.max)
            }?.quantum
    }

    private fun powerSetByWeight(presents: List<Int>, targetWeight: Int): Map<Int, Group>
    {
        return presents.fold(listOf(Group.empty)) { old, present ->
            old + old.map { it.addPresent(present) }.filter { it.weight <= targetWeight }
        }
            .groupingBy { it.weight }
            .reduce { _, min, candidate -> minOf(min, candidate) }
    }

    private data class Group(val count: Int, val weight: Int, val quantum: Long) : Comparable<Group>
    {
        fun addPresent(o: Int) = Group(count + 1, weight + o, quantum * o)
        fun mergeWith(o: Group) = Group(count + o.count, weight + o.weight, quantum * o.quantum)

        companion object
        {
            val empty = Group(0, 0, 1L)
            val max = Group(1000, 1000000, 100000000L)
        }

        override fun compareTo(other: Group) = compareValuesBy(this, other, Group::weight, Group::count, Group::quantum)
    }
}
