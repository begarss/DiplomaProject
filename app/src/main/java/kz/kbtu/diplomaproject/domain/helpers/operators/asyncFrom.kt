package kz.kbtu.diplomaproject.domain.helpers.operators

fun <T> asyncFrom(provider: suspend () -> T): Async<T> = AsyncFromLambda(provider)

class AsyncFromLambda<T>(private val provider: suspend () -> T) : Async<T> {
  override suspend fun consume(consumer: Consumer<T>) {
    consumer.onResult(provider())
  }
}