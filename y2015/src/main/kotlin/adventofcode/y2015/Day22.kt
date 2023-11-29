package adventofcode.y2015

import adventofcode.io.AdventSolution

object Day22 : AdventSolution(2015, 22, "Wizard Simulator 20XX") {

	override fun solvePartOne(input: String): String = parseInput(input).let { (hp, damage) -> play(hp, damage, false) }
	override fun solvePartTwo(input: String): String = parseInput(input).let { (hp, damage) -> play(hp, damage, true) }

	private fun parseInput(input: String): Pair<Int, Int> {
		val hp = input.substringAfter(": ").substringBefore('\n').toInt()
		val dmg = input.substringAfterLast(": ").toInt()
		return hp to dmg
	}


	private fun play(hp: Int, damage: Int, hardMode: Boolean): String {
        val start = Combat(50, 500, hp, damage, 0, 0, 0, hardMode, 0)
        var bestScore = Int.MAX_VALUE
        return generateSequence(listOf(start)) { c ->
            c.filterNot { it.isVictorious() }.filter { it.cost < bestScore }
                .flatMap { it.actions() }
        }
            .takeWhile { it.isNotEmpty() }
            .flatMap { it.asSequence() }
            .filter { it.isVictorious() }
            .map { it.cost }
            .onEach { bestScore = bestScore.coerceAtMost(it) }
            .minOrNull()
            .toString()
    }
}

private data class Combat(
		private val playerHp: Int,
		private val mana: Int,
		private val monsterHp: Int,
		private val monsterDamage: Int,
		private val shieldTimer: Int,
		private val poisonTimer: Int,
		private val rechargeTimer: Int,
		private val hardMode: Boolean,
		val cost: Int) {

	fun actions() = listOf(castMagicMissile(), castDrain(), castPoison(), castRecharge(), castShield())
			.mapNotNull { it?.tickRound() }
			.filter { it.playerHp > 0 }

	private fun castMagicMissile() = useMana(53)?.copy(monsterHp = monsterHp - 4)
	private fun castDrain() = useMana(73)?.copy(monsterHp = monsterHp - 2, playerHp = playerHp + 2)
	private fun castShield() = useMana(113)?.takeIf { shieldTimer == 0 }?.copy(shieldTimer = 6)
	private fun castPoison() = useMana(173)?.takeIf { poisonTimer == 0 }?.copy(poisonTimer = 6)
	private fun castRecharge() = useMana(229)?.takeIf { rechargeTimer == 0 }?.copy(rechargeTimer = 5)

	private fun useMana(burn: Int) = copy(mana = mana - burn, cost = cost + burn).takeIf { it.mana > 0 }

	private fun tickRound() = tickSpells().tickMonster().tickSpells().tickHardMode()

	private fun tickHardMode() = if (hardMode) copy(playerHp = playerHp - 1) else this

	private fun tickSpells() = copy(
			mana = mana + 101 * rechargeTimer.coerceAtMost(1),
			monsterHp = monsterHp - 3 * poisonTimer.coerceAtMost(1),
			shieldTimer = (shieldTimer - 1).coerceAtLeast(0),
			poisonTimer = (poisonTimer - 1).coerceAtLeast(0),
			rechargeTimer = (rechargeTimer - 1).coerceAtLeast(0)
	)

	private fun tickMonster() = if (isVictorious()) this else copy(playerHp = playerHp - monsterDamage + 7 * shieldTimer.coerceAtMost(1))

	fun isVictorious() = monsterHp <= 0

}
