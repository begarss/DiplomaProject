package kz.kbtu.diplomaproject.domain.helpers.operators

import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

fun <T> Async<T>.performOn(context: CoroutineContext): Async<T> {
  require(context[Job] == null) {
    "Context cannot contain job in it. Had $context"
  }
  return AsyncWithCoroutineContext(this, context)
}

class AsyncWithCoroutineContext<T>(
  private val source: Async<T>,
  private val context: CoroutineContext
) : Async<T> {
  override suspend fun consume(consumer: Consumer<T>) {
    val currentContext = coroutineContext

    withContext(context) {
      source.consume { value ->
        withContext(currentContext) {
          consumer.onResult(value)
        }
      }
    }
  }
}