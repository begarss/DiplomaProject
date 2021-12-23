package kz.kbtu.diplomaproject.presentation.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import okhttp3.MultipartBody

class ProfileViewModel(private val profileInteractor: ProfileInteractor) : BaseViewModel() {
  private val _profileState = MutableStateFlow<UserInfo?>(null)
  val profileState: StateFlow<UserInfo?> = _profileState

  fun getUserInfo() {
    profileInteractor.getUserInfo()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          _profileState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }

  fun setUserImage(file: MultipartBody.Part) {
    profileInteractor.setUserImage(file)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        Log.d("TAGA", "setUserImage: $it")
      }.onError {
        Log.d("TAGA", "setUserImage: $it")
      }
      .launchIn(viewModelScope)
  }

}