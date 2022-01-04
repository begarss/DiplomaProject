package kz.kbtu.diplomaproject.presentation.auth

import kz.kbtu.diplomaproject.data.backend.auth.RegistrationBody
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.AuthService

interface AuthInteractor {
  fun createUser(body: RegistrationBody): Async<DataResult<Boolean>>
  fun login(email: String, password: String): Async<DataResult<Boolean>>
  fun changePassword(oldPassword: String, newPassword: String): Async<DataResult<Boolean>>
  fun logout(): Async<DataResult<Boolean>>
  fun sendOtp(email: String): Async<DataResult<Boolean>>
  fun verifyOtp(email: String, otp: String): Async<DataResult<Boolean>>
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

  override fun changePassword(
    oldPassword: String,
    newPassword: String
  ) = async { authService.changePassword(oldPassword, newPassword) }

  override fun logout() = async {
    authService.logout()
  }

  override fun sendOtp(email: String) = async {
    authService.sendOtp(email)
  }

  override fun verifyOtp(email: String, otp: String) = async {
    authService.verifyOtp(email = email, otp = otp)
  }
}