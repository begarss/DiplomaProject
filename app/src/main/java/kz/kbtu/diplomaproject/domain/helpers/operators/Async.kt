package kz.kbtu.diplomaproject.domain.helpers.operators

typealias AsyncJob = Async<Unit>

interface Async<out T : Any?> {
  suspend fun consume(consumer: Consumer<T>)
}