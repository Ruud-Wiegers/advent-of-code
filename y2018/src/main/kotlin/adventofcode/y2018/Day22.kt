package adventofcode.y2018

import adventofcode.io.AdventSolution
import java.util.*

object Day22 : AdventSolution(2018, 22, "Mode Maze") {

    override fun solvePartOne(input: String): Int {
        val (depth, tx, ty) = parse(input)

        val cave = buildMaze(depth, tx, ty)
        cave[ty][tx] = 0

        return cave.sumOf(IntArray::sum)
    }

    override fun solvePartTwo(input: String): Int? {
        val (depth, tx, ty) = parse(input)

        val cave = buildMaze(depth, tx + 50, ty + 50)
        cave[ty][tx] = 0

        val start = CavePoint(0, 0, Equipment.Torch)
        val goal = CavePoint(tx, ty, Equipment.Torch)

        return findPath(start, goal, cave)

    }

    private fun buildMaze(depth: Int, tx: Int, ty: Int): List<IntArray> {
        val m = 20183

        val erosionLevel = List(ty + 1) { IntArray(tx + 1) }

        for (x in 0..tx) erosionLevel[0][x] = (x * 16807 + depth) % m
        for (y in 0..ty) erosionLevel[y][0] = (y * 48271 + depth) % m
        for (x in 1..tx) {
            for (y in 1..ty) {
                erosionLevel[y][x] = (erosionLevel[y - 1][x] * erosionLevel[y][x - 1] + depth) % m
            }
        }

        erosionLevel.forEach { row ->
            row.indices.forEach { row[it] %= 3 }
        }

        return erosionLevel

    }

    private fun parse(input: String) = input
            .filter { it in "0123456789 ," }
            .split(' ', ',')
            .filterNot(String::isBlank)
            .map(String::toInt)

}

private enum class Equipment { Torch, Climbing, Neither }
private data class CavePoint(val x: Int, val y: Int, val equipment: Equipment) {
    fun neighbors(cave: List<IntArray>): List<Pair<CavePoint, Int>> {

        val list = mutableListOf<Pair<CavePoint, Int>>()
        if (x > 0) list.add(CavePoint(x - 1, y, equipment) to 1)
        if (y > 0) list.add(CavePoint(x, y - 1, equipment) to 1)
        if (x < cave[0].lastIndex) list.add(CavePoint(x + 1, y, equipment) to 1)
        if (y < cave.lastIndex) list.add(CavePoint(x, y + 1, equipment) to 1)

        if (equipment != Equipment.Torch) list.add(CavePoint(x, y, Equipment.Torch) to 7)
        if (equipment != Equipment.Climbing) list.add(CavePoint(x, y, Equipment.Climbing) to 7)
        if (equipment != Equipment.Neither) list.add(CavePoint(x, y, Equipment.Neither) to 7)

        return list.filter { (s, _) ->
            when (cave[s.y][s.x]) {
                0 -> s.equipment != Equipment.Neither
                1 -> s.equipment != Equipment.Torch
                2 -> s.equipment != Equipment.Climbing
                else -> false
            }
        }

    }
}

//TODO make this a less botched pathfinding implementation
private fun findPath(start: CavePoint, goal: CavePoint, cave: List<IntArray>): Int? {
    val openList = PriorityQueue(compareBy<PathfinderStateW> { it.cost })
    openList.add(PathfinderStateW(start, 0))
    val distances = mutableMapOf(start to 0)

    while (openList.isNotEmpty()) {
        val candidate = openList.poll()
        if (candidate.st == goal) {
            return candidate.cost
        } else {
            candidate.move(cave).forEach {
                val existing = distances[it.st] ?: 10000
                if (it.cost < existing) {
                    distances[it.st] = it.cost
                    openList.add(PathfinderStateW(it.st, it.cost))
                }
            }
        }
    }
    return null
}

private data class PathfinderStateW(val st: CavePoint, val cost: Int) {
    fun move(cave: List<IntArray>): List<PathfinderStateW> = st.neighbors(cave).map { (s, d) -> PathfinderStateW(s, cost + d) }
}
