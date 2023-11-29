package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day21 : AdventSolution(2015, 21, "RPG Simulator 20XX") {

    override fun solvePartOne(input: String): Int {
        val bossMonster = parseInput(input)

        return itemSets()
            .first { equipment -> defeats(Character(100, equipment.damage, equipment.armor), bossMonster) }
            .cost
    }

    override fun solvePartTwo(input: String): Int {
        val bossMonster = parseInput(input)

        return itemSets()
            .last { equipment -> !defeats(Character(100, equipment.damage, equipment.armor), bossMonster) }
            .cost
    }


    private fun parseInput(input: String) = input.lines()
        .map { it.substringAfter(": ").toInt() }
        .let { (hp, dmg, ar) -> Character(hp, dmg, ar) }
}

private data class Character(val hp: Int, val dmg: Int, val ar: Int)

private fun defeats(player: Character, monster: Character): Boolean {
    val playerDamage = (player.dmg - monster.ar).coerceAtLeast(1)
    val turnsToWin = (monster.hp + playerDamage - 1) / playerDamage
    val monsterDamage = (monster.dmg - player.ar).coerceAtLeast(1)
    val turnsToLose = (player.hp + monsterDamage - 1) / monsterDamage
    return turnsToWin <= turnsToLose
}


private fun itemSets(): List<Item> = Item.rings
    .flatMapIndexed { index: Int, item: Item ->
        Item.rings.drop(index).map { item.combine(it) }
    }
    .flatMap { set -> Item.weapons.map(set::combine) }
    .flatMap { set -> Item.armor.map(set::combine) }
    .sortedBy(Item::cost)


//required
private data class Item(val cost: Int, val damage: Int, val armor: Int) {
    fun combine(o: Item) = Item(cost + o.cost, damage + o.damage, armor + o.armor)

    companion object {
        val empty = Item(0, 0, 0)

        val weapons = listOf(
            Item(8, 4, 0),
            Item(10, 5, 0),
            Item(25, 6, 0),
            Item(40, 7, 0),
            Item(74, 8, 0)
        )
        val armor = listOf(
            empty,
            Item(13, 0, 1),
            Item(31, 0, 2),
            Item(53, 0, 3),
            Item(75, 0, 4),
            Item(102, 0, 5)
        )
        val rings = listOf(
            empty,
            empty,
            Item(25, 1, 0),
            Item(50, 2, 0),
            Item(100, 3, 0),
            Item(20, 0, 1),
            Item(40, 0, 2),
            Item(80, 0, 3)
        )
    }
}




