package kz.kbtu.diplomaproject.presentation.base

import kz.kbtu.diplomaproject.data.common.TokenInfo
import kz.kbtu.diplomaproject.domain.services.UserService

interface UserInteractor {
  fun getToken(): TokenInfo
}

class UserInteractorImpl(private val userService: UserService) : UserInteractor {
  override fun getToken() = userService.getToken()
}