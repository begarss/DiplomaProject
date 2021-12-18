package kz.kbtu.diplomaproject.domain.helpers.operators

import kz.kbtu.diplomaproject.domain.model.DataResult

suspend fun <T : Any?> safeCall(call: suspend () -> T): DataResult<T> =
  try {
    DataResult.Success(call.invoke())
  } catch (throwable: Throwable) {
    when (throwable) {
      else -> {
        DataResult.Error(error = throwable.localizedMessage)
      }
    }
  }
