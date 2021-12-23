package kz.kbtu.diplomaproject.presentation.profile

import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.ProfileService
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part

interface ProfileInteractor {
  fun getUserInfo(): Async<DataResult<UserInfo?>>
  fun setUserImage(file: Part): Async<DataResult<UserInfo?>>
  fun setUserInfo(
    userName: String?,
    birthDay: String?,
    phone: String?
  ): Async<DataResult<UserInfo?>>
}

class ProfileInteractorImpl(private val profileService: ProfileService) : ProfileInteractor,
  CoroutineInteractor {
  override fun getUserInfo() = async {
    profileService.getUserInfo()
  }

  override fun setUserImage(file: Part) = async {
    profileService.setUserImage(file)
  }

  override fun setUserInfo(
    userName: String?,
    birthDay: String?,
    phone: String?
  ) = async {
    profileService.setUserInfo(userName = userName, birthDay = birthDay, phone = phone)
  }

}