package adventofcode.y2020

import adventofcode.io.AdventSolution

fun main() = Day21.solve()

object Day21 : AdventSolution(2020, 21, "Allergen Assessment")
{
    override fun solvePartOne(input: String): Int
    {
        val entries = input.lines().map(::parseEntry)
        val allergens = findAllergens(entries).values.reduce(Set<String>::union)
        return entries.flatMap { it.first.toList() }.count { it !in allergens }
    }

    override fun solvePartTwo(input: String): Any
    {
        val entries = input.lines().map(::parseEntry)

        var allergenCandidateMap = findAllergens(entries)

        val map: Map<String, String> = buildMap {
            while (allergenCandidateMap.isNotEmpty())
            {
                val (allergen, ingredient) = allergenCandidateMap.entries.first { it.value.size == 1 }
                this[allergen] = ingredient.single()
                allergenCandidateMap = allergenCandidateMap.mapValues { it.value - ingredient }.filterValues { it.isNotEmpty() }
            }
        }

        return map.toSortedMap().values.joinToString(",")
    }

    private fun parseEntry(input: String): Pair<Set<String>, List<String>>
    {
        val ingredients = input.substringBefore(" (").split(" ").toSet()
        val allergens = input.substringAfter("(contains ").substringBefore(")").split(", ")
        return ingredients to allergens
    }

    private fun findAllergens(entries: List<Pair<Set<String>, List<String>>>) = entries
        .flatMap { (ingredients, allergens) -> allergens.map { it to ingredients } }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.reduce(Set<String>::intersect) }
}
