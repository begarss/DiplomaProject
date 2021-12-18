package kz.kbtu.diplomaproject.presentation.auth

import kz.kbtu.diplomaproject.data.backend.auth.AuthApi
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationBody
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.AuthService

interface AuthInteractor {
  fun createUser(body: RegistrationBody): Async<DataResult<Boolean>>
  fun login(email: String, password: String): Async<DataResult<Boolean>>
}

class AuthInteractorImpl(
  private val authService: AuthService
) : AuthInteractor, CoroutineInteractor {
  override fun createUser(body: RegistrationBody) = async {
    authService.register(request = body)
  }

  override fun login(email: String, password: String) = async {
    authService.login(email = email, password = password)
  }
}