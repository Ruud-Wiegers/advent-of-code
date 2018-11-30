package adventofcode.util

fun <T> permute(input: List<T>): Sequence<List<T>> = input.fold(sequenceOf(listOf())) { permutations, elementToAdd ->
	permutations.flatMap { permutation ->
		(0..permutation.size).asSequence().map { i -> permutation.take(i) + elementToAdd + permutation.drop(i) }
	}
}