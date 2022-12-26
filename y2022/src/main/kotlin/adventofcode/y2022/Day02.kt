package adventofcode.y2022

import adventofcode.AdventSolution

object Day02 : AdventSolution(2022, 2, "Rock Paper Scissors") {

    override fun solvePartOne(input: String) = input
        .lines()
        .map { line -> line.first().toRps() to line.last().toRps() }
        .sumOf { (opp, play) -> play.score + score(play, opp) }


    override fun solvePartTwo(input: String): Any {
        return input.lines()
            .map { line -> line.first().toRps() to line.last().toOutcome() }
            .map { (opp, strat) -> opp to strategize(opp, strat) }
            .sumOf { (opp, play) -> play.score + score(play, opp) }
    }
}

private fun score(play: RPS, opponent: RPS) = when (opponent) {
    play.losesTo() -> 0
    play.draws() -> 3
    else -> 6
}


private enum class RPS(
    val score: Int
) {
    Rock(1), Paper(2), Scissors(3);


    fun beats() = when (this) {
        Rock -> Scissors
        Paper -> Rock
        Scissors -> Paper
    }

    fun draws() = this

    fun losesTo() = beats().beats()

}

private fun Char.toRps() = when (this) {
    'A', 'X' -> RPS.Rock
    'B', 'Y' -> RPS.Paper
    'C', 'Z' -> RPS.Scissors
    else -> throw IllegalArgumentException()
}


private enum class Outcome { Lose, Draw, Win }

private fun Char.toOutcome() = when (this) {
    'X' -> Outcome.Lose
    'Y' -> Outcome.Draw
    'Z' -> Outcome.Win
    else -> throw IllegalArgumentException()
}


private fun strategize(opponent: RPS, outcome: Outcome): RPS = when (outcome) {
    Outcome.Lose -> opponent.beats()
    Outcome.Draw -> opponent.draws()
    Outcome.Win -> opponent.losesTo()
}