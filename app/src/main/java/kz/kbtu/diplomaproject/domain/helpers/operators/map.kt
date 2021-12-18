package kz.kbtu.diplomaproject.domain.helpers.operators

fun <T, R> Async<T>.map(tranform: suspend (T) -> R): Async<R> = AsyncTransform(this, tranform)

class AsyncTransform<T, R>(
  private val source: Async<T>,
  private val tranform: suspend (T) -> R
) : Async<R> {
  override suspend fun consume(consumer: Consumer<R>) {
    source.consume { value -> consumer.onResult(tranform(value)) }
  }
}