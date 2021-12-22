package kz.kbtu.diplomaproject.presentation.profile

import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.ProfileService

interface ProfileInteractor {
  fun getUserInfo(): Async<DataResult<UserInfo?>>
}

class ProfileInteractorImpl(private val profileService: ProfileService) : ProfileInteractor,
  CoroutineInteractor {
  override fun getUserInfo() = async {
    profileService.getUserInfo()
  }

}