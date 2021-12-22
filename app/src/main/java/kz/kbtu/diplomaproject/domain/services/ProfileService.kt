package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.profile.ProfileApi
import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface ProfileService {
  suspend fun getUserInfo(): DataResult<UserInfo?>
}

class ProfileServiceImpl(private val profileApi: ProfileApi) : ProfileService {
  override suspend fun getUserInfo(): DataResult<UserInfo?> = safeCall {
    val response = profileApi.getUserInfo()
    val body = response.body()
    body
  }

}