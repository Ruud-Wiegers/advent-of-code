package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() = Day20.solve()

object Day20 : AdventSolution(2020, 20, "Jurassic Jigsaw") {
    override fun solvePartOne(input: String): Any {
        val tiles = parseInput(input)
        val unmatchedEdges = groupByUnmatchedCount(tiles)
        return unmatchedEdges[2]!!.reduce(Long::times)
    }

    override fun solvePartTwo(input: String): Int {
        val tiles = parseInput(input)

        val unmatchedEdges = groupByUnmatchedCount(tiles)

        val corners = unmatchedEdges.getValue(2).toSet()
        val edges = unmatchedEdges.getValue(1).toSet()
        require(corners.size == 4)
        require(edges.size == 40)

        val arrangedTiles = arrangePuzzle(tiles.associateBy { it.id }, corners, edges)

        val image = cutTiles(arrangedTiles)

        val allOrientations =
            generateSequence(Tile(0, image), Tile::rotate).take(4).let { it + it.map(Tile::flip) }.map { it.grid }

        val monster = listOf(
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   "
        )
        val monsterCount = allOrientations.map { countMonsters(it, monster) }.first { it > 0 }

        return image.sumOf { it.count { it == '#' } } - monsterCount * monster.sumOf { it.count { it == '#' } }
    }

    private fun countMonsters(image: List<String>, monster: List<String>): Int {
        fun List<String>.transpose(): List<String> =
            first().indices.map { index ->
                asSequence().map { it[index] }.joinToString("")
            }


        val slices =
            image.windowed(monster.size) { it.transpose().windowed(monster[0].length).map { it.transpose() } }.flatten()

        fun matches(slice: List<String>, monster: List<String>): Boolean {
            for (y in monster.indices) {
                for (x in monster[0].indices) {
                    if (monster[y][x] != '#') continue
                    if (slice[y][x] != '#') return false
                }
            }
            return true
        }

        return slices.count { matches(it, monster) }
    }


    private fun arrangePuzzle(tiles: Map<Long, Tile>, corners: Set<Long>, edges: Set<Long>): List<List<Tile>> {
        val middleTiles = tiles.keys.filter { it !in corners && it !in edges }.toSet()

        val fullTiles =
            tiles.mapValues { generateSequence(it.value, Tile::rotate).take(4).let { it + it.map(Tile::flip) } }


        fun solve(
            g: List<List<Tile>>,
            p: Vec2,
            unusedCorners: Set<Long>,
            unusedEdges: Set<Long>,
            unusedMiddles: Set<Long>
        ): List<List<Tile>>? {
            fun inc() = if (p.x < 11) p.copy(x = p.x + 1) else p.copy(y = p.y + 1, x = 0)

            if (p.y == 12) return g

            fun Int.edge() = this == 0 || this == 11

            val options = when {
                p.x.edge() && p.y.edge() -> unusedCorners
                p.x.edge() || p.y.edge() -> unusedEdges
                else                     -> unusedMiddles
            }

            fun placeTile(g: List<List<Tile>>, tile: Tile): List<List<Tile>> =
                if (p.x == 0) g.plusElement(listOf(tile))
                else g.dropLast(1).plusElement(g.last() + tile)


            fun isValidPlacement(tile:Tile):Boolean{
                if (p.x > 0) {
                    if (g[p.y][p.x-1].right!= tile.left) return false
                }
                if (p.y > 0) {
                    if (g[p.y-1][p.x].bottom != tile.top) return false
                }
                return true
            }

            return options.asSequence()
                .flatMap { id -> fullTiles.getValue(id).asSequence().map { id to it } }
                .filter { isValidPlacement(it.second) }
                .mapNotNull { (id, tile) ->
                    solve(
                        placeTile(g, tile),
                        inc(),
                        unusedCorners - id,
                        unusedEdges - id,
                        unusedMiddles - id
                    )
                }
                .firstOrNull()

        }
        return solve(listOf(), Vec2.origin, corners, edges, middleTiles)!!
    }

    private fun cutTiles(arrangedTiles: List<List<Tile>>): List<String> = arrangedTiles.flatMap {
        val map = it.map { it.grid.drop(1).dropLast(1) }
        map.first().indices.map { index -> map.map { it[index] } }
            .map { it.map { it.drop(1).dropLast(1) }.joinToString("") }
    }


    private fun groupByUnmatchedCount(tiles: List<Tile>): Map<Int, List<Long>> {
        val edgeGroups = tiles
            .flatMap { t -> t.allEdges.map { it to t } }
            .groupBy({ it.first }, { it.second })


        val umatchedEdges = edgeGroups.values.mapNotNull { it.singleOrNull() }.groupingBy { it.id }.eachCount()
            .mapValues { it.value / 2 }
        return umatchedEdges.entries.groupBy({ it.value }, { it.key })
    }

    private fun parseInput(input: String): List<Tile> = input.split("\n\n")
        .map { it.lines() }
        .map { Tile(it[0].drop(5).dropLast(1).toLong(), it.drop(1)) }


    data class Tile(val id: Long, val grid: List<String>) {

        val top = grid.first()
        val right = grid.map(String::last).joinToString("")
        val bottom = grid.last()
        val left = grid.map(String::first).joinToString("")

        fun flip() = copy(grid = grid.map(String::reversed))
        fun rotate() = copy(grid = grid.indices.map { y -> grid.indices.map { x -> grid[grid.lastIndex-x][y] }.joinToString("") })

        val allEdges by lazy { listOf(top, right, bottom, left).let { it + it.map(String::reversed) } }

    }
}
