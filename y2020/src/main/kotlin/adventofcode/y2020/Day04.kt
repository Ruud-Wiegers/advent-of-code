package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day04.solve()

object Day04 : AdventSolution(2020, 4, "Passport Processing")
{
    override fun solvePartOne(input: String): Int
    {
        val keys = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

        return input
            .splitToSequence("\n\n")
            .map(::parseToDocument)
            .count { keys.all(it::containsKey) }
    }

    override fun solvePartTwo(input: String): Int
    {
        val hclValidator = "#[a-f0-9]{6}".toRegex()
        val eclValidator = "amb|blu|brn|gry|grn|hzl|oth".toRegex()
        val pidValidator = "[0-9]{9}".toRegex()

        val validators = sequenceOf<Validator>(
            { (it["byr"]?.toIntOrNull() ?: 0) in 1920..2002 },
            { (it["iyr"]?.toIntOrNull() ?: 0) in 2010..2020 },
            { (it["eyr"]?.toIntOrNull() ?: 0) in 2020..2030 },
            {
                val hgt = it.getOrDefault("hgt", "")
                when (hgt.takeLast(2))
                {
                    "cm" -> (hgt.dropLast(2).toIntOrNull() ?: 0) in 150..193
                    "in" -> (hgt.dropLast(2).toIntOrNull() ?: 0) in 59..76
                    else -> false
                }
            },
            { it.getOrDefault("hcl", "").let(hclValidator::matches) },
            { it.getOrDefault("ecl", "").let(eclValidator::matches) },
            { it.getOrDefault("pid", "").let(pidValidator::matches) }
        )

        return input
            .splitToSequence("\n\n")
            .map(::parseToDocument)
            .count { document -> validators.all { v -> v(document) } }
    }

    private fun parseToDocument(input: String) = input
        .splitToSequence(' ', '\n')
        .map { it.split(':') }
        .associate { (k, v) -> k to v }
}

private typealias Validator = (Map<String, String>) -> Boolean
