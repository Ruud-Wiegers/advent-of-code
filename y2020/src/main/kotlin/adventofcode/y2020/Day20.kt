package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Vec2

fun main() = Day20.solve()

object Day20 : AdventSolution(2020, 20, "Jurassic Jigsaw")
{
    override fun solvePartOne(input: String): Any
    {
        val unmatchedEdges = unmatchedEdgeCounts(parseInput(input))

        return unmatchedEdges.filter { it.value == 2 }.keys.reduce(Long::times)
    }

    override fun solvePartTwo(input: String): Int
    {
        val tiles: List<Tile> = parseInput(input)

        val unmatchedEdges = unmatchedEdgeCounts(tiles)

        val tilesByEdgecount: List<Map<Long, Tile>> =
            tiles.groupBy { unmatchedEdges[it.id] ?: 0 }.toSortedMap().values.map { it.associateBy { it.id } }

        val arrangedTiles = arrangePuzzle(listOf(), Vec2.origin, tilesByEdgecount).first()

        val image = cutTiles(arrangedTiles)

        val monster = Image(
            listOf(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
            )
        )
        val monsterCount = image.allOrientations().map { countMonsters(it, monster) }.first { it > 0 }

        fun Image.count() = grid.joinToString().count { it == '#' }

        return image.count() - monsterCount * monster.count()
    }

    private fun countMonsters(image: Image, monster: Image): Int
    {

        fun matches(slice: List<String>, monster: List<String>): Boolean
        {
            for (y in monster.indices)
            {
                for (x in monster[0].indices)
                {
                    if (monster[y][x] != '#') continue
                    if (slice[y][x] != '#') return false
                }
            }
            return true
        }

        return image.sequenceOfSlices(monster.grid[0].length, monster.grid.size)
            .count { matches(it.grid, monster.grid) }
    }

    private fun arrangePuzzle(grid: List<List<Tile>>, p: Vec2, tilesByEdgecount: List<Map<Long, Tile>>): Sequence<List<List<Tile>>>
    {
        if (p.y == 12) return sequenceOf(grid)

        val edges = listOf(p.x == 0, p.x == 11, p.y == 0, p.y == 11).count { it }

        fun isValidPlacement(tile: Tile): Boolean = when
        {
            p.x > 0 && grid[p.y][p.x - 1].right != tile.left -> false
            p.y > 0 && grid[p.y - 1][p.x].bottom != tile.top -> false
            else                                             -> true
        }

        fun placeTile(tile: Tile): List<List<Tile>> =
            if (p.x == 0) grid.plusElement(listOf(tile))
            else grid.dropLast(1).plusElement(grid.last() + tile)

        fun nextPosition() = if (p.x < 11) p.copy(x = p.x + 1) else p.copy(y = p.y + 1, x = 0)

        fun updateRemainingTiles(tile: Tile) = tilesByEdgecount.toMutableList().apply { this[edges] = this[edges].minus(tile.id) }

        return tilesByEdgecount[edges].values.asSequence()
            .flatMap(Tile::allOrientations)
            .filter(::isValidPlacement)
            .flatMap { tile -> arrangePuzzle(placeTile(tile), nextPosition(), updateRemainingTiles(tile)) }
    }

    private fun cutTiles(arrangedTiles: List<List<Tile>>): Image = arrangedTiles
        .map { line ->
            line.map { it.img.cropped(1, 1, it.img.width - 2, it.img.height - 2) }
        }
        .map { Image.stitchHorizontal(it) }
        .let { Image.stitchVertical(it) }

    private fun unmatchedEdgeCounts(tiles: List<Tile>): Map<Long, Int> = tiles
        .flatMap { t -> t.allEdges.map { it to t } }
        .groupBy({ it.first }, { it.second })
        .values
        .mapNotNull { it.singleOrNull() }
        .groupingBy { it.id }
        .eachCount()
        .mapValues { it.value / 2 }

    private fun parseInput(input: String): List<Tile> = input.split("\n\n")
        .map { it.lines() }
        .map { Tile(it[0].drop(5).dropLast(1).toLong(), Image(it.drop(1))) }
}

private data class Tile(val id: Long, val img: Image)
{
    val top = img.grid.first()
    val right = img.column(img.width - 1)
    val bottom = img.grid.last()
    val left = img.column(0)

    val allOrientations by lazy { img.allOrientations().map { Tile(id, it) } }

    val allEdges by lazy { listOf(top, right, bottom, left).let { it + it.map(String::reversed) } }
}

private data class Image(val grid: List<String>)
{
    val width = grid[0].length
    val height = grid.size

    fun flipped() = grid.map(String::reversed).let(::Image)

    fun rotated() = grid[0].indices.reversed().map(this::column).let(::Image)

    fun cropped(x: Int, y: Int, width: Int, height: Int) = grid
        .slice(y until y + height)
        .map { line -> line.slice(x until x + width) }
        .let(::Image)

    fun column(x: Int) = grid.map { it[x] }.toCharArray().let(::String)

    fun allOrientations(): List<Image> =
        generateSequence(this, Image::rotated).take(4).toList().let { it.map(Image::flipped) + it }

    fun sequenceOfSlices(width: Int, height: Int) = sequence {
        for (y in 0 until grid.size - height)
            for (x in 0 until grid[0].length - width)
                yield(cropped(x, y, width, height))
    }

    companion object
    {
        fun stitchHorizontal(images: List<Image>) = Image(images.first().grid.indices.map { y ->
            images.joinToString("") { it.grid[y] }
        })

        fun stitchVertical(images: List<Image>) = Image(images.flatMap(Image::grid))
    }
}