package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.common.TokenInfo
import kz.kbtu.diplomaproject.data.storage.Preferences

interface UserService {
  fun getToken(): TokenInfo
}

class UserServiceImpl(private val preferences: Preferences) : UserService {
  override fun getToken(): TokenInfo {
    return preferences.getTokenInfo()
  }

}