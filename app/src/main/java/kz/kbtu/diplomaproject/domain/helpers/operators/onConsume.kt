package kz.kbtu.diplomaproject.domain.helpers.operators

fun <T> Async<T>.onConsume(onConsumeAction: suspend () -> Unit): Async<T> =
  AsyncWithOnConsumeAction(this, onConsumeAction)

class AsyncWithOnConsumeAction<T>(
  private val source: Async<T>,
  private val onConsumeAction: suspend () -> Unit
) : Async<T> {
  override suspend fun consume(consumer: Consumer<T>) {
    onConsumeAction()
    source.consume(consumer)
  }
}