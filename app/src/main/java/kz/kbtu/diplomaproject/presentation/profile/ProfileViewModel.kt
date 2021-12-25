package kz.kbtu.diplomaproject.presentation.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.profile.userInfo.EditUserFragment.Companion.BIRTH_PATTERN
import kz.kbtu.diplomaproject.presentation.profile.userInfo.EditUserFragment.Companion.REQUEST_BIRTH_PATTERN
import okhttp3.MultipartBody
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel(private val profileInteractor: ProfileInteractor) : BaseViewModel() {
  private val _profileState = MutableStateFlow<UserInfo?>(null)
  val profileState: StateFlow<UserInfo?> = _profileState

  private val _editState = MutableStateFlow(value = false)
  val editState: StateFlow<Boolean> = _editState

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

  fun setUserInfo(
    userName: String?,
    birthDay: String?,
    phone: String?
  ) {
    val inputFormat: DateFormat = SimpleDateFormat(BIRTH_PATTERN, Locale.getDefault())
    val outputFormat: DateFormat =
      SimpleDateFormat(REQUEST_BIRTH_PATTERN, Locale.getDefault())

    val date: String? = if (!birthDay.isNullOrEmpty()) {
      outputFormat.format(inputFormat.parse(birthDay))
    } else {
      null
    }

    profileInteractor.setUserInfo(userName, date, phone)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult { result ->
        if (result.isSuccess()) {
          _editState.emit(true)
        }
        if (result.isError()) {
          _editState.emit(false)
        }
      }.launchIn(viewModelScope)
  }

  fun clearState() {
    viewModelScope.launch {
      _editState.emit(false)
    }
  }
}