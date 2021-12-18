package kz.kbtu.diplomaproject.domain.helpers.operators

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface CoroutineInteractor {
  fun <T : Any?> asyncFlow(job: () -> T): Flow<T> = flow { emit(job()) }.flowOn(Dispatchers.IO)

  fun <T : Any?> backgroundFlow(flowProvider: () -> Flow<T>): Flow<T> =
    flowProvider().flowOn(Dispatchers.IO)

  fun <T : Any?> async(function: suspend () -> T): Async<T> =
    asyncFrom(function).performOn(Dispatchers.IO)

  fun asyncJob(action: suspend () -> Unit): AsyncJob = asyncFrom(action).performOn(Dispatchers.IO)
}