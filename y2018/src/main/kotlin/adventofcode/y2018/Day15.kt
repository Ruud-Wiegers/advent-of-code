package adventofcode.y2018

import adventofcode.AdventSolution
import kotlin.math.abs

object Day15 : AdventSolution(2018, 15, "Beverage Bandits") {

    override fun solvePartOne(input: String): Int {
        val map = parse(input)
        val combatants = mutableListOf<Combatant>()
        for (y in map.indices) {
            for (x in map[0].indices) {
                if (map[y][x] == Tile.Goblin) combatants.add(Combatant(Point(x, y), 200, 3, false))
                if (map[y][x] == Tile.Elf) combatants.add(Combatant(Point(x, y), 200, 3, true))
            }
        }

        val r = simulate(map, combatants)
        return r * combatants.sumOf { it.hp }
    }

    override fun solvePartTwo(input: String): Int {

        for (power in 4..30) {
            val map = parse(input)
            val combatants = mutableListOf<Combatant>()
            for (y in map.indices) {
                for (x in map[0].indices) {
                    if (map[y][x] == Tile.Goblin) combatants.add(Combatant(Point(x, y), 200, 3, false))
                    if (map[y][x] == Tile.Elf) combatants.add(Combatant(Point(x, y), 200, power, true))
                }
            }

            val elves = combatants.filter { it.isElf }

            val r = simulate(map, combatants)

            if (elves.any { !it.isAlive() }) continue
            return r * combatants.sumOf { it.hp }
        }
        return -1
    }

    private fun simulate(map: MutableList<MutableList<Tile>>, combatants: MutableList<Combatant>): Int {
        var rounds = 0


        while (true) {
            for (attacker in combatants.sortedBy { it.p.x }.sortedBy { it.p.y }) {
                if (!attacker.isAlive()) continue

                if (combatants.distinctBy { it.isElf }.size == 1) return rounds

                val target: Combatant? = findAdjacentTarget(attacker, combatants)

                if (target == null) moveTo(attacker, map, combatants)

                findAdjacentTarget(attacker, combatants)
                        ?.let {
                            it.hp -= attacker.power
                            if (!it.isAlive()) {
                                combatants.remove(it)
                                map[it.p.y][it.p.x] = Tile.Open
//                                if (!attacker.isElf) return -1
                            }
                        }
            }
            rounds++
        }
    }

    private fun findAdjacentTarget(attacker: Combatant, combatants: MutableList<Combatant>): Combatant? =
            combatants
                    .asSequence()
                    .filter { it.isValidTarget(attacker) }
                    .sortedBy { it.p.x }
                    .sortedBy { it.p.y }
                    .sortedBy { it.hp }
                    .firstOrNull()


    private fun moveTo(attacker: Combatant, map: MutableList<MutableList<Tile>>, combatants: MutableList<Combatant>) {
        val targetsByDistance = combatants
                .asSequence()
                .filter { it.isElf != attacker.isElf }
                .filter { it.hp > 0 }
                .flatMap { it.p.neighbors().asSequence() }
                .distinct()
                .filter { map.isOpen(it) }
                .associateWith { distanceTo(attacker.p, it, map) }
        val min = targetsByDistance.values.minOrNull() ?: return
        if (min == Int.MAX_VALUE) return
        val destination = targetsByDistance.keys.sortedBy { it.x }.sortedBy { it.y }.find { targetsByDistance[it] == min }
                ?: return


        val stepTo = attacker.p.neighbors()
            .filter { map.isOpen(it) }
            .minByOrNull { distanceTo(it, destination, map) } ?: return


        map[attacker.p.y][attacker.p.x] = Tile.Open
        attacker.p = stepTo
        map[attacker.p.y][attacker.p.x] = if (attacker.isElf) Tile.Elf else Tile.Goblin
    }

    private fun distanceTo(from: Point, to: Point, map: MutableList<MutableList<Tile>>): Int {
        var open = listOf(from)
        val closed = mutableSetOf<Point>()
        var steps = 0

        while (open.isNotEmpty()) {
            if (open.any { it == to }) return steps
            steps++
            closed += open
            open = open
                    .flatMap { it.neighbors() }
                    .distinct()
                    .filter { it !in closed }
                    .filter { map.isOpen(it) }
        }
        return Int.MAX_VALUE
    }

    private fun List<List<Tile>>.isOpen(it: Point) = getOrNull(it.y)?.getOrNull(it.x) == Tile.Open

    data class Point(val x: Int, val y: Int) {
        fun neighbors() =
                listOf(Point(x, y - 1), Point(x - 1, y), Point(x + 1, y), Point(x, y + 1))

    }

    data class Combatant(var p: Point, var hp: Int, val power: Int, val isElf: Boolean) {
        fun isValidTarget(attacker: Combatant) = isAdjecentTo(attacker) && isElf != attacker.isElf && isAlive()
        fun isAdjecentTo(attacker: Combatant) =
                abs(p.y - attacker.p.y) + abs(p.x - attacker.p.x) == 1

        fun isAlive() = hp > 0
    }

    private fun parse(input: String): MutableList<MutableList<Tile>> {
        return input.lines().map {
            it.map { tile ->
                when (tile) {
                    '#' -> Tile.Closed
                    '.' -> Tile.Open
                    'E' -> Tile.Elf
                    'G' -> Tile.Goblin
                    else -> throw IllegalArgumentException("unknown")
                }
            }.toMutableList()

        }.toMutableList()
    }

    private enum class Tile {
        Open, Closed, Elf, Goblin
    }
}
