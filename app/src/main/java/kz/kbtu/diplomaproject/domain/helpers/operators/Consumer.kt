package kz.kbtu.diplomaproject.domain.helpers.operators

interface Consumer<in T : Any?> {
  suspend fun onResult(value: T)
}

val EmptyConsumer = object : Consumer<Any?> {
  override suspend fun onResult(value: Any?) = Unit
}