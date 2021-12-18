package kz.kbtu.diplomaproject.data.backend.auth

data class RegistrationBody(
  val email: String,
  val password: String,
  val password2: String,
)
