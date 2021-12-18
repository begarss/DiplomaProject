package kz.kbtu.diplomaproject.domain.helpers.operators

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

suspend fun <T> Async<T>.consume(consumer: suspend (T) -> Unit) {
  val consumerObj = object : Consumer<T> {
    override suspend fun onResult(value: T) {
      consumer(value)
    }
  }
  consume(consumerObj)
}

suspend fun <T> Async<T>.consume() = this.consume(EmptyConsumer)

fun <T> Async<T>.launchIn(scope: CoroutineScope): Job =
  scope.launch { this@launchIn.consume(EmptyConsumer) }
