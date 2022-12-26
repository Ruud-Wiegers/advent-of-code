package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import java.io.StreamTokenizer
import java.io.StringReader


object Day22 : AdventSolution(2022, 22, "Monkey Map") {

    override fun solvePartOne(input: String): Int = solve(input) { grid ->

        val xs = grid.flatMapIndexed { y, row ->
            val xl = row.indexOfFirst { it != Terrain.Air }
            val xr = row.indexOfLast { it != Terrain.Air }

            listOf(
                Turtle(Vec2(xl, y), Direction.LEFT) to Turtle(Vec2(xr, y), Direction.LEFT),
                Turtle(Vec2(xr, y), Direction.RIGHT) to Turtle(Vec2(xl, y), Direction.RIGHT)
            )
        }

        val ys = grid.first().indices.flatMap { x ->
            val yt = grid.indexOfFirst { it.getOrElse(x) { Terrain.Air } != Terrain.Air }
            val yb = grid.indexOfLast { it.getOrElse(x) { Terrain.Air } != Terrain.Air }

            listOf(
                Turtle(Vec2(x, yt), Direction.UP) to Turtle(Vec2(x, yb), Direction.UP),
                Turtle(Vec2(x, yb), Direction.DOWN) to Turtle(Vec2(x, yt), Direction.DOWN)
            )
        }

        (xs + ys).toMap()

    }

    override fun solvePartTwo(input: String) = solve(input) {
        class Line(val start: Vec2, val dir: Direction, val size: Int) {
            val exit = dir.turnRight
            val enter = dir.turnLeft

            fun asSource() =
                generateSequence(start) { it + dir.vector }.take(size).map { Turtle(it, exit) }.toList().reversed()

            fun asTarget() = generateSequence(start) { it + dir.vector }.take(size).map { Turtle(it, enter) }.toList()

        }

        fun stitch(one: Line, two: Line) = one.asSource().zip(two.asTarget()) + two.asSource().zip(one.asTarget())
        //net:
        // FR
        // D
        //LB
        //U
        val fl = Line(Vec2(50, 0), Direction.DOWN, 50)
        val fu = Line(Vec2(99, 0), Direction.LEFT, 50)
        val ru = Line(Vec2(149, 0), Direction.LEFT, 50)
        val rr = Line(Vec2(149, 49), Direction.UP, 50)
        val rd = Line(Vec2(100, 49), Direction.RIGHT, 50)
        val dl = Line(Vec2(50, 50), Direction.DOWN, 50)
        val dr = Line(Vec2(99, 99), Direction.UP, 50)
        val ll = Line(Vec2(0, 100), Direction.DOWN, 50)
        val lu = Line(Vec2(49, 100), Direction.LEFT, 50)
        val br = Line(Vec2(99, 149), Direction.UP, 50)
        val bd = Line(Vec2(50, 149), Direction.RIGHT, 50)
        val ul = Line(Vec2(0, 150), Direction.DOWN, 50)
        val ur = Line(Vec2(49, 199), Direction.UP, 50)
        val ud = Line(Vec2(0, 199), Direction.RIGHT, 50)
        val glueLines = sequenceOf(
            fl, ll,
            fu, ul,
            rd, dr,
            lu, dl,
            ur, bd,
            rr, br,
            ru, ud
        )
        glueLines.chunked<Line, List<Pair<Turtle, Turtle>>>(2) { (a, b) -> stitch(a, b) }
            .flatten()
            .toMap()
    }

    private fun solve(input: String, map: (List<List<Terrain>>) -> Map<Turtle, Turtle>): Int {
        val (grid, path) = parse(input)


        val initial = Vec2(grid[0].indexOfFirst { it == Terrain.Floor }, 0)

        val maze = MonkeyMap(grid, map(grid))


        val stretchedPath = path.flatMap {
            when (it) {
                Move.Left -> listOf(Move.Left)
                Move.Right -> listOf(Move.Right)
                is Move.Forward -> List(it.steps) { Move.Forward(1) }
            }
        }

        val end = stretchedPath.fold(Turtle(initial, Direction.RIGHT)) { old, move ->
            when (move) {
                Move.Left -> old.copy(d = old.d.turnLeft)
                Move.Right -> old.copy(d = old.d.turnRight)
                is Move.Forward -> maze.step(old).let { if (maze[it.p] == Terrain.Floor) it else old }
            }
        }

        return 1000 * (end.p.y + 1) + 4 * (end.p.x + 1) + when (end.d) {
            Direction.UP -> 3
            Direction.RIGHT -> 0
            Direction.DOWN -> 1
            Direction.LEFT -> 2
        }
    }

    private fun parse(input: String): Pair<List<List<Terrain>>, Sequence<Move>> {
        val (mazeStr, pathStr) = input.split("\n\n")
        val maze = parseMaze(mazeStr)

        val path = tokenize(pathStr)
        return maze to path
    }


    private fun parseMaze(input: String) = input.lines().map { line ->
        line.map { ch ->
            when (ch) {
                ' ' -> Terrain.Air
                '.' -> Terrain.Floor
                '#' -> Terrain.Wall
                else -> throw IllegalStateException()
            }
        }
    }


    private fun tokenize(str: String): Sequence<Move> = sequence {
        val reader = StringReader(str)
        val tokenizer = StreamTokenizer(reader)
        tokenizer.ordinaryChar('L'.code)
        tokenizer.ordinaryChar('R'.code)

        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            val tokenValue = when (tokenizer.ttype) {
                StreamTokenizer.TT_NUMBER -> Move.Forward(tokenizer.nval.toInt())
                'L'.code -> Move.Left
                'R'.code -> Move.Right
                else -> throw IllegalArgumentException(tokenizer.ttype.toString())
            }
            yield(tokenValue)
        }
    }

    sealed class Move {
        object Left : Move()
        object Right : Move()
        data class Forward(val steps: Int) : Move()
    }

    enum class Terrain { Air, Floor, Wall }
}


private class MonkeyMap(grid: List<List<Day22.Terrain>>, val wrap: Map<Turtle, Turtle>) {

    val floor = grid
        .flatMapIndexed { y, row ->
            row.mapIndexed { x, terrain -> Vec2(x, y) to terrain }
        }
        .toMap()
        .filterValues { it != Day22.Terrain.Air }


    operator fun get(position: Vec2) = floor[position] ?: Day22.Terrain.Air

    fun step(turtle: Turtle): Turtle =
        (wrap[turtle] ?: turtle.copy(p = turtle.p + turtle.d.vector))


}

private data class Turtle(val p: Vec2, val d: Direction)