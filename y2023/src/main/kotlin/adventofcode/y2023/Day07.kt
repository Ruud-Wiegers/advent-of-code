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
            Hand(hand, bid.toInt())
        }

        return hands.sorted().mapIndexed { i, h -> (i + 1L) * h.bid }.sum()
    }

    override fun solvePartTwo(input: String): Long {
        val hands = input.lines().map {
            val (hand, bid) = it.split(" ")
            JokerHand(hand, bid.toInt())
        }

        return hands.sorted().mapIndexed { i, h -> (i + 1L) * h.bid }.sum()
    }
}


private open class Hand(str: String, val bid: Int) : Comparable<Hand> {

    open val cardValues = "23456789TJQKA"

    val hand: List<Card> by lazy { str.map { Card(it, cardValues) } }

    open val handType: HandType by lazy {
        hand.groupingBy { it }.eachCount().values.sorted().let(HandType::fromCardFrequency)
    }

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


private class JokerHand(str: String, bid: Int) : Hand(str, bid) {

    override val cardValues = "J23456789TQKA"

    override val handType: HandType by lazy {
        val freq = hand.groupingBy { it }.eachCount()
        val jokers = freq[joker] ?: 0
        val other = freq.minus(joker).values.sorted()
        val best = other.dropLast(1) + ((other.lastOrNull() ?: 0) + jokers)

        HandType.fromCardFrequency(best)
    }
}

private val joker = Card('J', "J23456789TQKA")



private enum class HandType(val freq: List<Int>) {
    HighCard(listOf(1, 1, 1, 1, 1)),
    OnePair(listOf(1, 1, 1, 2)),
    TwoPair(listOf(1, 2, 2)),
    ThreeOfAKind(listOf(1, 1, 3)),
    FullHouse(listOf(2, 3)),
    FourOfAKind(listOf(1, 4)),
    FiveOfAKind(listOf(5));

    companion object {
        fun fromCardFrequency(freq: List<Int>) = HandType.entries.first { freq == it.freq }
    }
}

private class Card(card: Char, ordering: String) : Comparable<Card> {

    private val value = ordering.indexOf(card)
    override fun compareTo(other: Card): Int = value.compareTo(other.value)
    override fun equals(other: Any?): Boolean = (other as? Card)?.value == value
    override fun hashCode() = value.hashCode()
}

