package adventofcode.util.algorithm

fun List<String>.transposeString(): List<String> =
    first().indices.map { index ->
        buildString {
            for (row in this@transposeString) {
                append(row[index])
            }
        }
    }

fun <T> List<List<T>>.transpose(): List<List<T>> =
    this[0].indices.map { col -> map { it[col] } }