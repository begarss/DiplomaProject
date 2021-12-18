package kz.kbtu.diplomaproject.domain.model

sealed class DataResult<out T> {
  data class Success<out T>(val data: T) : DataResult<T>()

  data class Error(
    val error: String?
  ) : DataResult<Nothing>()

  object UnauthorizedError : DataResult<Nothing>()

  fun isSuccess(): Boolean = this is Success
  fun isError(): Boolean = this is Error

  fun dataValue(): T? = (this as? Success)?.data

  fun errorValue(): String? = (this as? Error)?.error
}
