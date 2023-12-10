package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Direction.*
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.toGrid

fun main() {
    Day10.solve()
}

object Day10 : AdventSolution(2023, 10, "Pipe Maze") {

    override fun solvePartOne(input: String): Any? {

        val grid = input.lines().flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Vec2(x, y) to ch }
        }.toMap()

        val start = grid.filterValues { it == 'S' }.keys.first()


        val direction = Direction.entries.first { it.reverse in grid[start + it.vector]!!.let(edges::getValue) }

        val seq = generateSequence(Pair(start, direction)) { (pos, dir) ->
            val newPos = pos + dir.vector
            val newDir = grid[newPos]!!.let(edges::getValue).first { it != dir.reverse }
            newPos to newDir
        }


        val result = seq.drop(1).indexOfFirst { grid[it.first] == 'S' }

        return (result + 1) / 2

    }

    override fun solvePartTwo(input: String): Int {

        val grid = input.lines().flatMapIndexed { y, line ->
            line.mapIndexed { x, ch -> Vec2(x, y) to ch }
        }.toMap()

        val startPosition = grid.filterValues { it == 'S' }.keys.first()

        val connections = Direction.entries.filter { it.reverse in (grid[startPosition + it.vector]?.let { edges[it]} ?: emptyList()) }.toSet()

        val startSymbol = edges.entries.first { it.value == connections }.key

        val seq = generateSequence(Pair(startPosition, connections.first())) { (pos, dir) ->
            val newPos = pos + dir.vector
            val newDir = edges[grid[newPos]!!]!!.first { it != dir.reverse }
            newPos to newDir
        }


        val pipes = seq.drop(1).takeWhile { grid[it.first] != 'S' }.map { it.first }.toSet()

        val pipeMap = grid.filterKeys { it in pipes } + (startPosition to startSymbol)

        return pipeMap.toGrid(' ').sumOf { row ->

            row.scan(Pair(true, false)) { (_, isInside), ch ->
                Pair((ch == ' '), (ch in "|7F") xor isInside)
            }
                .count { (open, inside) -> open && inside }


        }


    }

}

/* failed attempt at flood fill

      val leftSide = mutableSetOf<Vec2>()
      val rightSide = mutableSetOf<Vec2>()
      val pipe = mutableSetOf(start)

      seq.drop(1).takeWhile { grid[it.first] != 'S' }.forEach { (position, facing) ->
          when {
              facing in grid[position]!!.edges() -> {
                  leftSide += position + facing.turnLeft.vector
                  rightSide += position + facing.turnRight.vector
              }

              facing.turnLeft in grid[position]!!.edges() -> {
                  rightSide += listOf(position + facing.turnRight.vector, position + facing.vector)
              }

              facing.turnRight in grid[position]!!.edges() -> {
                  leftSide += listOf(position + facing.turnLeft.vector, position + facing.vector)
              }

          }

          pipe += position
      }




      //TODO include edges of starting point ... not needed usually

      val inside: MutableSet<Vec2> = leftSide.takeIf { it.all(grid::contains) } ?: rightSide


      val open = inside.filter { it !in pipe }.toMutableSet()


      while (true) {
          val new = open.flatMap(Vec2::neighbors).filter { it !in open && it !in pipe }
          open += new

          println(open.size)
          if (new.isEmpty()) break
      }


      val show: SparseGrid<Char> = grid.mapValues { (k, v) -> if (k in open) 'I' else if (k !in pipe) ' ' else '█' }


      show.toGrid(" ").forEach { println(it.joinToString("")) }

 return open.size

       */



private val edges = mapOf(
    '|' to setOf(UP, DOWN),
    '-' to setOf(LEFT, RIGHT),
    'L' to setOf(UP, RIGHT),
    'J' to setOf(UP, LEFT),
    '7' to setOf(DOWN, LEFT),
    'F' to setOf(DOWN, RIGHT),
    '.' to emptySet(),
    'S' to setOf(UP,DOWN,LEFT,RIGHT)
)