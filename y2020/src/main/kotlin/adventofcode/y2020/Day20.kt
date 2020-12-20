package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day20.solve()

object Day20 : AdventSolution(2020, 20, "Jurassic Jigsaw")
{
    override fun solvePartOne(input: String): Any
    {
        val tiles = input.split("\n\n")
            .map { it.lines() }
            .map {
                val id = it[0].drop(5).dropLast(1)
                Tile(id.toLong(), it.drop(1))
            }

        val edgeGroups= tiles.flatMap { t-> t.edges.map {  it to t }}.groupBy( { it.first },{it.second})

println(tiles.count())

        val v = edgeGroups.filterValues { it.size==1 }
        return v.values.map { it[0].id }.groupingBy { it }.eachCount().toSortedMap().filterValues { it==4 }.keys.reduce(Long::times)
    }

    data class Tile(val id: Long, val grid: List<String>)
    {

        val edges by lazy { listOf(grid.first(),
                                   grid.last(),
                                   grid.map { it[0] }.joinToString(""),
                                   grid.map { it.last() }.joinToString(""))
            .let { it + it.map { it.reversed() } } }
    }

    override fun solvePartTwo(input: String): Long
    {
        return 0
    }
}
