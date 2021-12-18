package kz.kbtu.diplomaproject.domain.helpers.operators

fun <T> Async<T>.onResult(resultConsumer: suspend (T) -> Unit): Async<T> =
  AsyncWithResultConsumer(this, resultConsumer)

class AsyncWithResultConsumer<T>(
  private val source: Async<T>,
  private val resultConsumer: suspend (T) -> Unit
) : Async<T> {
  override suspend fun consume(consumer: Consumer<T>) {
    source.consume { value ->
      resultConsumer(value)
      consumer.onResult(value)
    }
  }
}