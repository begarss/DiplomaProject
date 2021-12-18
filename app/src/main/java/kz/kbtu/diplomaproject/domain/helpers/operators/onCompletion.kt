package kz.kbtu.diplomaproject.domain.helpers.operators

fun <T> Async<T>.onCompletion(onCompletionAction: suspend (e: Throwable?) -> Unit): Async<T> =
  AsyncWithOnCompletionAction(this, onCompletionAction)

class AsyncWithOnCompletionAction<T>(
  private val source: Async<T>,
  private val onConsumeAction: suspend (e: Throwable?) -> Unit
) : Async<T> {
  override suspend fun consume(consumer: Consumer<T>) {
    try {
      source.consume(consumer)
      onConsumeAction(null)
    } catch (e: Throwable) {
      onConsumeAction(e)
      throw e
    }

  }
}