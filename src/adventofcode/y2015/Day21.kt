package adventofcode.y2015

import adventofcode.AdventSolution

object Day21 : AdventSolution(2015, 21, "RPG Simulator 20XX") {

	override fun solvePartOne(input: String): String {

		val bossMonster = parseInput(input)

		return createItemSets()
				.sortedBy { it.cost }
				.filter { equipment -> combat(Character(100, equipment.damage, equipment.armor), bossMonster) }
				.first()
				.cost
				.toString()
	}

	override fun solvePartTwo(input: String): String {

		val bossMonster = parseInput(input)

		return createItemSets()
				.sortedBy { -it.cost }
				.filterNot { equipment -> combat(Character(100, equipment.damage, equipment.armor), bossMonster) }
				.first()
				.cost
				.toString()
	}

	private fun createItemSets(): Sequence<Item> {
		return itemSets()
				.map { it.reduce { a, b -> Item(a.cost + b.cost, a.damage + b.damage, a.armor + b.armor) } }
				.distinct()
	}


	private fun parseInput(input: String) =
			input.split("\n").map { it.substringAfter(": ").toInt() }.let { (hp, dmg, ar) -> Character(hp, dmg, ar) }
}

private fun combat(player: Character, monster: Character): Boolean {
	val playerDamage = (player.dmg - monster.ar).coerceAtLeast(1)
	val turnsToWin = (monster.hp + playerDamage - 1) / playerDamage
	val monsterDamage = (monster.dmg - player.ar).coerceAtLeast(1)
	val turnsToLose = (player.hp + monsterDamage - 1) / monsterDamage
	return turnsToWin <= turnsToLose
}


private fun itemSets() = sequence {
	weapons.forEach { w ->
		yield(setOf(w))
		armor.forEach { a ->
			yield(setOf(w, a))
			rings.forEach { r1 ->
				yield(setOf(w, a, r1))
				yield(setOf(w, r1)) //duplicates
				rings.filterNot { it == r1 }.forEach { r2 ->
					yield(setOf(w, a, r1, r2))
					yield(setOf(w, r1, r2))
				}
			}
		}
	}
}

private data class Character(val hp: Int, val dmg: Int, val ar: Int)

private val weapons = setOf(
		Item(8, 4, 0),
		Item(10, 5, 0),
		Item(25, 6, 0),
		Item(40, 7, 0),
		Item(74, 8, 0))
private val armor = setOf(
		Item(13, 0, 1),
		Item(31, 0, 2),
		Item(53, 0, 3),
		Item(75, 0, 4),
		Item(102, 0, 5)
)
private val rings = setOf(
		Item(25, 1, 0),
		Item(50, 2, 0),
		Item(100, 3, 0),
		Item(20, 0, 1),
		Item(40, 0, 2),
		Item(80, 0, 3)
)

private data class Item(val cost: Int, val damage: Int, val armor: Int)




