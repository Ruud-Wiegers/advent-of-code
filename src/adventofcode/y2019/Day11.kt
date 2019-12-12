package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day11.solve()

object Day11 : AdventSolution(2019, 11, "Space Police") {

    override fun solvePartOne(input: String): Int {
        val hull = mutableMapOf<Point, Long>()
        paint(input, hull)
        return hull.size
    }
    override fun solvePartTwo(input: String): String {
        val hull = mutableMapOf(Point(0,0)to 1L)
        paint(input, hull)

        val maxX= hull.keys.maxBy{it.x}!!.x
        val maxY = hull.keys.maxBy{it.y}!!.y

       return List(maxY+1){ y->
            List<String>(maxX+1){x-> if(hull[Point(x,y)]==1L) "██" else "  "}.joinToString ("")
        }.joinToString("\n","\n")


    }

    private fun paint(input: String, hull: MutableMap<Point, Long>) {
        var direction = Direction.UP
        var position = Point(0, 0)
        val ant = parseProgram(input)

        while (ant.state != IntCodeProgram.State.Halted) {
            ant.input(hull[position] ?: 0)
            ant.execute()
            ant.output()?.let { hull[position] = it }
            ant.output()?.let { direction = if (it == 0L) direction.left() else direction.right() }
            position += direction.vector
        }
    }


    private fun parseProgram(data: String) = data
            .split(',')
            .map(String::toLong)
            .let{ IntCodeProgram(it) }

    private data class Point(val x:Int,val y:Int){
        operator fun plus(o:Point)= Point(x+o.x,y+o.y)
    }
    private enum class Direction(val vector: Point) {
        UP(Point(0, -1)), RIGHT(Point(1, 0)), DOWN(Point(0, 1)), LEFT(Point(-1, 0));

        fun left() = turn(3)
        fun right() = turn(1)

        private fun turn(i: Int): Direction = Direction.values().let { it[(it.indexOf(this) + i) % it.size] }
    }
}
