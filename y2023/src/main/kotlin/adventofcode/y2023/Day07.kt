package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2023, 7, "???") {

    override fun solvePartOne(input: String): Long {
        val hands = input.lines().map {
            val (hand, bid) = it.split(" ")
            Hand(hand.map { Card(it, cardValues) }, bid.toInt())
        }

        return hands.sorted().mapIndexed { i, h -> (i + 1L) * h.bid }.sum()
    }

    override fun solvePartTwo(input: String): Long {
        val hands = input.lines().map {
            val (hand, bid) = it.split(" ")
            JokerHand(hand.map { Card(it, jokerCardValues) }, bid.toInt())
        }

        return hands.sorted().mapIndexed { i, h -> (i + 1L) * h.bid }.sum()
    }
}


private open class Hand(val hand: List<Card>, val bid: Int) : Comparable<Hand> {


    val freq = hand.groupingBy { it }.eachCount()

    open val handType: HandType by lazy {  freq.values.sorted().toHandType() }


    override fun compareTo(other: Hand): Int = compareValuesBy(
        this,
        other,
        { it.handType },
        { it.hand[0] },
        { it.hand[1] },
        { it.hand[2] },
        { it.hand[3] },
        { it.hand[4] })

}


private class JokerHand(hand: List<Card>, bid: Int) : Hand(hand,bid) {
    override val handType: HandType by lazy {
        val jokers = freq[joker] ?: 0
        val other = freq.minus(joker).values.sorted()
        val best = other.dropLast(1) + ((other.lastOrNull() ?: 0) + jokers)

        best.toHandType()
    }
}


private enum class HandType(val freq: List<Int>) {
    HighCard(listOf(1, 1, 1, 1, 1)),
    OnePair(listOf(1, 1, 1, 2)),
    TwoPair(listOf(1, 2, 2)),
    ThreeOfAKind(listOf(1, 1, 3)),
    FullHouse(listOf(2, 3)),
    FourOfAKind(listOf(1, 4)),
    FiveOfAKind(listOf(5))
}

private fun List<Int>.toHandType() = HandType.entries.first { this == it.freq }
private data class Card(val card: Char, val ordering: String) : Comparable<Card> {

    private val value = ordering.indexOf(card)
    override fun compareTo(other: Card): Int = value.compareTo(other.value)
}

private const val cardValues = "23456789TJQKA"

private const val jokerCardValues = "J23456789TQKA"

private val joker = Card('J', jokerCardValues)
