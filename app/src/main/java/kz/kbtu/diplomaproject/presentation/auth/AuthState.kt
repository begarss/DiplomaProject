package kz.kbtu.diplomaproject.presentation.auth

enum class AuthState {
  EMPTY,
  VALID,
  INVALID,
  USER_EXIST,
  USER_NOT_EXIST
}