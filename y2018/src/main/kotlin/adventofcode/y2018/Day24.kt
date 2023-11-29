package adventofcode.y2018

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() = Day24.solve()

object Day24 : AdventSolution(2018, 24, "Immune System Simulator 20XX") {

    override fun solvePartOne(input: String): Int {
        val (immune, disease) = parse(input).chunked(10).toList()

        val (remI, remD) = runCombat(immune, disease)
        return maxOf(remI, remD)
    }

    override fun solvePartTwo(input: String): Int {
        val (immune, disease) = parse(input).chunked(10).toList()
        val x = 61 //binary search run by hand for now
        val (remI, _) = runCombat(immune.map { it.copy(damage = it.damage + x) }, disease)
        return remI
    }

    private fun runCombat(immune: List<Group>, disease: List<Group>): Pair<Int, Int> {
        while (immune.any { it.count > 0 } && disease.any { it.count > 0 }) {
            (selectTargets(immune, disease) + selectTargets(disease, immune))
                    .sortedByDescending { it.first.initiative }
                    .forEach { (att, def) -> att.dealDamage(def) }
        }

        return Pair(immune.sumOf(Group::count), disease.sumOf(Group::count))
    }

    private fun selectTargets(attackers: List<Group>, defenders: List<Group>): List<Pair<Group, Group>> {
        val validTargets = defenders.filter { it.count > 0 }.toMutableSet()
        return attackers
                .sortedWith(compareByDescending(Group::effectivePower).thenByDescending(Group::initiative))
                .mapNotNull { attacker ->
                    attacker.chooseTarget(validTargets)?.let { target ->
                        validTargets -= target
                        attacker to target
                    }
                }
    }
}

private data class Group(
        var count: Int,
        val hp: Int,
        val weak: Set<Damage>,
        val immune: Set<Damage>,
        val damage: Int,
        val type: Damage,
        val initiative: Int
) {
    fun effectivePower() = count * damage

    fun chooseTarget(targets: Iterable<Group>): Group? {
        return targets.sortedWith(compareByDescending(Group::effectivePower).thenByDescending(Group::initiative))
                .maxByOrNull(this::calcDamage)
                ?.takeUnless { calcDamage(it) == 0 }
    }

    fun dealDamage(target: Group) {
        target.count -= minOf(calcDamage(target) / target.hp, target.count)
    }

    private fun calcDamage(target: Group) =
            when (type) {
                in target.immune -> 0
                in target.weak -> effectivePower() * 2
                else -> effectivePower()
            }
}

private enum class Damage { Slashing, Bludgeoning, Cold, Fire, Radiation }

private fun String.toDamageType() = Damage.valueOf(replaceFirstChar(Char::titlecase))

private fun parse(input: String): Sequence<Group> {
    val regex = "(\\d+) units each with (\\d+) hit points(.*) with an attack that does (\\d+) (.*) damage at initiative (\\d+)".toRegex()
    return input
            .lineSequence()
            .map { regex.matchEntire(it) }
            .filterNotNull()
            .map { it.destructured }
            .map { (c, h, imm, d, t, i) ->
                val weak = imm.parseImmunityList("weak")
                val immune = imm.parseImmunityList("immune")
                Group(c.toInt(), h.toInt(), weak, immune, d.toInt(), t.toDamageType(), i.toInt())
            }
}

private fun String.parseImmunityList(modifier: String) =
        substringAfter("$modifier to ", "")
                .substringBefore(';')
                .trim(' ', ')')
                .splitToSequence(", ")
                .filter { it.isNotBlank() }
                .map { it.toDamageType() }
                .toSet()