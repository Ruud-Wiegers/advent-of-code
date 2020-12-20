package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() = Day20.solve()

object Day20 : AdventSolution(2020, 20, "Jurassic Jigsaw") {
    override fun solvePartOne(input: String): Any {
        val tiles = parseInput(input)
        val unmatchedEdges = unmatchedEdgeCounts(tiles)

        return unmatchedEdges.filter { it.value == 2 }.keys.reduce(Long::times)
    }

    override fun solvePartTwo(input: String): Int {
        val tiles: List<Tile> = parseInput(input)

        val unmatchedEdges = unmatchedEdgeCounts(tiles)

        val tilesByEdgecount = tiles.groupBy { unmatchedEdges[it.id] ?: 0 }.toSortedMap().values.map { it.toSet() }

        val arrangedTiles = arrangePuzzle(tilesByEdgecount)

        val image = cutTiles(arrangedTiles)

        val monster = Image(
            listOf(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
            )
        )
        val monsterCount = image.sequenceOfOrientations().map { countMonsters(it, monster) }.first { it > 0 }

        return image.grid.sumOf { it.count { it == '#' } } - monsterCount * monster.grid.sumOf { it.count { it == '#' } }
    }

    private fun countMonsters(image: Image, monster: Image): Int {

        fun matches(slice: List<String>, monster: List<String>): Boolean {
            for (y in monster.indices) {
                for (x in monster[0].indices) {
                    if (monster[y][x] != '#') continue
                    if (slice[y][x] != '#') return false
                }
            }
            return true
        }

        return image.sequenceOfSlices(monster.grid[0].length, monster.grid.size)
            .count { matches(it.grid, monster.grid) }
    }


    private fun arrangePuzzle(tilesByEdgecount: List<Set<Tile>>): List<List<Tile>> {
        fun solve(g: List<List<Tile>>, p: Vec2, tilesByEdgecount: List<Set<Tile>>): List<List<Tile>>? {
            fun inc() = if (p.x < 11) p.copy(x = p.x + 1) else p.copy(y = p.y + 1, x = 0)

            if (p.y == 12) return g

            val edges = listOf(p.x == 0, p.x == 11, p.y == 0, p.y == 11).count { it }


            fun placeTile(g: List<List<Tile>>, tile: Tile): List<List<Tile>> =
                if (p.x == 0) g.plusElement(listOf(tile))
                else g.dropLast(1).plusElement(g.last() + tile)


            fun isValidPlacement(tile: Tile): Boolean = when {
                p.x > 0 && g[p.y][p.x - 1].right != tile.left -> false
                p.y > 0 && g[p.y - 1][p.x].bottom != tile.top -> false
                else                                          -> true
            }

            return tilesByEdgecount[edges].asSequence()
                .flatMap { tile -> tile.img.sequenceOfOrientations() //TODO this is slow, back to explicit preexpansion and lookup
                    .map { tile.copy(img = it) } }
                .filter { isValidPlacement(it) }
                .mapNotNull { tile ->
                    solve(
                        placeTile(g, tile),
                        inc(),
                        tilesByEdgecount.toMutableList().apply { this[edges] = this[edges] - tile })
                }
                .firstOrNull()

        }
        return solve(listOf(), Vec2.origin, tilesByEdgecount)!!
    }

    private fun cutTiles(arrangedTiles: List<List<Tile>>): Image = arrangedTiles
        .map { line ->
            line.map { it.img.cropped(1, 1, it.img.width - 2, it.img.height - 2) }
        }
        .map { Image.stitchHorizontal(it) }
        .let { Image.stitchVertical(it) }

    private fun unmatchedEdgeCounts(tiles: List<Tile>): Map<Long, Int> {
        val edgeGroups = tiles
            .flatMap { t -> t.allEdges.map { it to t } }
            .groupBy({ it.first }, { it.second })

        return edgeGroups.values.mapNotNull { it.singleOrNull() }.groupingBy { it.id }.eachCount()
            .mapValues { it.value / 2 }
    }

    private fun parseInput(input: String): List<Tile> = input.split("\n\n")
        .map { it.lines() }
        .map { Tile(it[0].drop(5).dropLast(1).toLong(), Image(it.drop(1))) }
}

private data class Tile(val id: Long, val img: Image) {
    val top = img.grid.first()
    val right = img.grid.map(String::last).joinToString("")
    val bottom = img.grid.last()
    val left = img.grid.map(String::first).joinToString("")

    val allEdges by lazy { listOf(top, right, bottom, left).let { it + it.map(String::reversed) } }
}

private data class Image(val grid: List<String>) {

    val width = grid[0].length
    val height = grid.size

    fun flipped() = Image(grid.map(String::reversed))

    fun rotated() = Image(grid.indices.map { y ->
        grid[0].indices.map { x ->
            grid[grid.lastIndex - x][y]
        }.joinToString("")
    })

    fun cropped(xs: IntRange, ys: IntRange) = Image(grid.slice(ys).map { line -> line.slice(xs) })

    fun cropped(x: Int, y: Int, width: Int, height: Int) = cropped(x until x + width, y until y + height)


    fun sequenceOfOrientations(): Sequence<Image> =
        generateSequence(this, Image::rotated).take(4).let { it + it.map(Image::flipped) }

    fun sequenceOfSlices(width: Int, height: Int) = sequence {
        for (y in 0 until grid.size - height)
            for (x in 0 until grid[0].length - width)
                yield(cropped(x, y, width, height))
    }

    companion object {
        fun stitchHorizontal(images: List<Image>) = Image(images.first().grid.indices.map { y ->
            images.joinToString("") { it.grid[y] }
        })

        fun stitchVertical(images: List<Image>) = Image(images.flatMap(Image::grid))
    }
}
