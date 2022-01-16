package kz.kbtu.diplomaproject.domain.model

enum class ErrorCode(val code: String) {
  USER_NOT_FOUND("User not found!!!"),
  WRONG_EMAIL("Wrong password."),
  ALREADY_EXIST("User already exists"),
  PASSWORDS_NOT_SAME("PASSWORD")
}