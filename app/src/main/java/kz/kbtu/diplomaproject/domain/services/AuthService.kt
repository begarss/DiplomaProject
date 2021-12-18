package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.auth.AuthApi
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationBody
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationResponse
import kz.kbtu.diplomaproject.data.storage.Preferences
import kz.kbtu.diplomaproject.domain.model.DataResult

interface AuthService {
  suspend fun register(request: RegistrationBody): DataResult<Boolean>
}

class AuthServiceImpl(
  private val authApi: AuthApi,
  private val preferences: Preferences
) : AuthService {
  override suspend fun register(request: RegistrationBody): DataResult<Boolean> =
    try {
      val response = authApi.register(body = request)
      if (response.isSuccessful) {
        val body = response.body()
        if (!body?.token.isNullOrEmpty()) {
          DataResult.Success(true)
        } else {
          DataResult.Success(false)
        }
      } else {
        DataResult.Error(response.message())
      }
    } catch (e: Throwable) {
      DataResult.Error(e.localizedMessage)
    }

}