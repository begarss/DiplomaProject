package kz.kbtu.diplomaproject.presentation.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.domain.model.ErrorCode
import kz.kbtu.diplomaproject.presentation.auth.AuthInteractor
import kz.kbtu.diplomaproject.presentation.auth.AuthState
import kz.kbtu.diplomaproject.presentation.auth.ChangeState
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.EMPTY
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.INVALID
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.VALID
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.WRONG_PASSWORD
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.profile.userInfo.EditUserFragment.Companion.BIRTH_PATTERN
import kz.kbtu.diplomaproject.presentation.profile.userInfo.EditUserFragment.Companion.REQUEST_BIRTH_PATTERN
import okhttp3.MultipartBody
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileViewModel(
  private val profileInteractor: ProfileInteractor,
  private val authInteractor: AuthInteractor
) : BaseViewModel() {
  private val _profileState = MutableStateFlow<UserInfo?>(null)
  val profileState: StateFlow<UserInfo?> = _profileState

  private val _editState = MutableStateFlow(value = false)
  val editState: StateFlow<Boolean> = _editState

  private val _changePswState = MutableStateFlow<ChangeState?>(value = EMPTY)
  val changePswState: StateFlow<ChangeState?> = _changePswState

  private val _logoutState = MutableStateFlow<Boolean?>(null)
  val logoutState: StateFlow<Boolean?> = _logoutState

  private val _authState = MutableStateFlow(AuthState.EMPTY)
  val authState: SharedFlow<AuthState>
    get() = _authState

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

  fun changePassword(oldPassword: String, newPassword: String) {
    authInteractor.changePassword(oldPassword, newPassword)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        Log.d("TAGA", "changePassword: $it")
        if (it.isSuccess()) {
          if (it.dataValue() == true) {
            _changePswState.emit(VALID)
          } else {
            _changePswState.emit(INVALID)
          }
        } else {
          if (it.errorValue() == ErrorCode.WRONG_EMAIL.code) {
            _changePswState.emit(WRONG_PASSWORD)
          } else {
            _changePswState.emit(INVALID)
          }
        }
      }
      .onError {
      }
      .launchIn(viewModelScope)
  }

  fun logout() {
    authInteractor.logout()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          if (it.dataValue() == true) {
            _logoutState.emit(true)
          } else {
            _logoutState.emit(false)
          }
        }
      }.launchIn(viewModelScope)
  }

  fun login(email: String, password: String) {
    authInteractor.login(email = email, password = password)
      .onResult {
        Log.d("TAGA", "login: $it")
        if (it.isSuccess()) {
          if (it.dataValue() == true)
            _authState.emit(AuthState.VALID)
          else {
            _authState.emit(AuthState.INVALID)
          }
        } else {
          if (it.errorValue() == ErrorCode.USER_NOT_FOUND.code) {
            _authState.emit(AuthState.USER_NOT_EXIST)
          }
          _authState.emit(AuthState.INVALID)
        }
      }
      .onError {
        Log.d("TAGA", "login: $it")
        _authState.emit(AuthState.INVALID)
      }.launchIn(viewModelScope)
  }

  fun clearState() {
    viewModelScope.launch {
      _editState.emit(false)
      _changePswState.emit(EMPTY)
    }
  }
}