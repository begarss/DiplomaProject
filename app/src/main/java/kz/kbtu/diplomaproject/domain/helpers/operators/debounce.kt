package kz.kbtu.diplomaproject.domain.helpers.operators

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> CoroutineScope.debounce(
  waitMs: Long = 500L,
  destinationFunction: (T) -> Unit
): (T) -> Unit {
  var debounceJob: Job? = null
  return { param: T ->
    debounceJob?.cancel()
    debounceJob = launch {
      delay(waitMs)
      destinationFunction(param)
    }
  }
}