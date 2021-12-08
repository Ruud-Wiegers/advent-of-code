package adventofcode.util.collections

fun <T> Iterable<T>.permutations(): Sequence<List<T>> =
    fold(sequenceOf(listOf())) { permutations, elementToAdd ->
        permutations.flatMap { permutation ->
            (0..permutation.size).asSequence().map { i -> permutation.take(i) + elementToAdd + permutation.drop(i) }
        }
    }