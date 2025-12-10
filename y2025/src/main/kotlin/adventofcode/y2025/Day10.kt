package adventofcode.y2025

import adventofcode.io.AdventSolution
import com.microsoft.z3.*


fun main() {
    Day10.solve()
}

object Day10 : AdventSolution(2025, 10, "Factory") {

    override fun solvePartOne(input: String) = input.lines().sumOf(::solveSingle)

    private fun solveSingle(input: String): Int {
        val (lights, buttons) = parse(input)
        val initial = lights.map { false }

        fun nextState(state: List<Boolean>) = buttons.map { state.zip(it, Boolean::xor) }

        return generateSequence(listOf(initial)) { it.flatMap(::nextState).distinct() }
            .indexOfFirst { it.any(lights::equals) }
    }

    override fun solvePartTwo(input: String) = input.lines().sumOf(::solveZ3)

    private fun solveZ3(input: String): Int {
        val (_, buttons, targets) = parse(input)

        // map from counter to each button that affects that counter
        val buttonPressesPerCounter: Map<Int, List<Int>> = buttons
            .flatMapIndexed { index, booleans ->
                booleans.withIndex().filter { it.value }.map { it.index to index }
            }.groupBy({ it.first }, { it.second })


        return Context().use { it.optimize(buttons.size, buttonPressesPerCounter, targets) }
    }

    private fun Context.optimize(
        buttonCount: Int,
        buttonPressesPerCounter: Map<Int, List<Int>>,
        counterTargets: List<Int>
    ): Int {
        val countButtonPressedExpressions: Array<IntExpr> = List(buttonCount) { index ->
            mkIntConst("button $index pressed")
        }.toTypedArray()

        val counterTargetValueReachedExpressions: List<BoolExpr> =
            buttonPressesPerCounter.map { (counterIndex, buttons) ->
                val buttonPressExpressions = buttons.map(countButtonPressedExpressions::get).toTypedArray()
                val sumOfButtonPressesExpression = mkAdd(*buttonPressExpressions)
                val counterTargetValueExpression: IntExpr = mkInt(counterTargets[counterIndex])

                mkEq(sumOfButtonPressesExpression, counterTargetValueExpression)
            }

        val sumOfButtonPressesExpression: ArithExpr<IntSort> = mkAdd(*countButtonPressedExpressions)

        val optimizationContext: Optimize = mkOptimize()

        counterTargetValueReachedExpressions.forEach(optimizationContext::Add)
        countButtonPressedExpressions.map { mkGe(it, mkInt(0)) }.forEach(optimizationContext::Add)
        val answer = optimizationContext.MkMinimize(sumOfButtonPressesExpression)

        optimizationContext.Check()


        return answer.value.let { it as IntNum }.int
    }
}

private data class Machine(val lights: List<Boolean>, val buttons: List<List<Boolean>>, val targets: List<Int>)

private fun parse(input: String): Machine {
    val split = input.split(" ")
    val lights = split[0].drop(1).dropLast(1).map { it == '#' }
    val buttons = split.drop(1).dropLast(1).map {
        it.drop(1).dropLast(1).split(",").map(String::toInt)
    }
        .map { lightsToToggle -> List(lights.size) { it in lightsToToggle } }

    val targets = split.last().drop(1).dropLast(1).split(",").map(String::toInt)
    return Machine(lights, buttons, targets)
}
