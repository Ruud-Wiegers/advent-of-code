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

        fun nextState(state: List<Boolean>) = buttons.map { state.zip(it, Boolean::xor) }

        return generateSequence(listOf(lights)) { it.flatMap(::nextState).distinct() }
            .indexOfFirst { it.any { state -> state.none { it } } }
    }

    override fun solvePartTwo(input: String) = input.lines().sumOf(::solveZ3)

    private fun solveZ3(input: String): Int {
        val (_, buttons, targets) = parse(input)

        // map from counter to each button that affects that counter
        val buttonPressesPerCounter: Map<Int, List<Int>> = buttons
            .flatMapIndexed { index, booleans ->
                booleans.withIndex().filter { it.value }.map { it.index to index }
            }
            .groupBy({ it.first }, { it.second })


        val ctx = Context()

        //create buttons
        val countButtonPressedExpressions =
            List(buttons.size) { index -> ctx.mkIntConst("times button $index is pressed") }.toTypedArray()


        // Each counter target value should be reached by pressing the corresponding buttons
        val counterTargetValueReachedExpressions: List<BoolExpr> =
            buttonPressesPerCounter.map { (counterIndex, buttons) ->
                val buttonPressExpressions = buttons.map(countButtonPressedExpressions::get).toTypedArray()
                val sumOfButtonPressesExpression = ctx.mkAdd(*buttonPressExpressions)
                val counterTargetValueExpression: IntExpr = ctx.mkInt(targets[counterIndex])

                ctx.mkEq(sumOfButtonPressesExpression, counterTargetValueExpression)
            }

        val sumOfButtonPressesExpression = ctx.mkAdd(*countButtonPressedExpressions)

        val optimizationContext: Optimize = ctx.mkOptimize()

        counterTargetValueReachedExpressions.forEach {
            optimizationContext.Add(it)
        }


        //require that each button is pressed 0+ times
        countButtonPressedExpressions.forEach { buttonVar ->
            optimizationContext.Add(ctx.mkGe(buttonVar, ctx.mkInt(0)))
        }

        optimizationContext.MkMinimize(sumOfButtonPressesExpression)

        optimizationContext.Check()
        return optimizationContext.model.evaluate(sumOfButtonPressesExpression, false)
            .let { it as IntNum }
            .int
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


