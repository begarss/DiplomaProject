package kz.kbtu.diplomaproject.domain.helpers.operators

import kotlinx.coroutines.CancellationException

fun <T> Async<T>.onError(errorConsumer: suspend (Throwable) -> Unit): Async<T> =
  AsyncErrorConsumer(this, errorConsumer)

class AsyncErrorConsumer<T>(
  private val source: Async<T>,
  private val errorConsumer: suspend (Throwable) -> Unit
) : Async<T> {
  override suspend fun consume(consumer: Consumer<T>) {
    var downStreamException: Throwable? = null
    try {
      source.consume { value ->
        try {
          consumer.onResult(value)
        } catch (e: Throwable) {
          downStreamException = e
          throw e
        }
      }

    } catch (e: Throwable) {
      if (e == downStreamException || e is CancellationException) {
        throw e
      } else {
        errorConsumer(e)
      }
    }
  }
}