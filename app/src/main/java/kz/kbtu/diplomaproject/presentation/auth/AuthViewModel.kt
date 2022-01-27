package kz.kbtu.diplomaproject.presentation.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationBody
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.domain.model.ErrorCode
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel

class AuthViewModel(private val authInteractor: AuthInteractor) : BaseViewModel() {

  private val _authState = MutableStateFlow(AuthState.EMPTY)
  val authState: SharedFlow<AuthState>
    get() = _authState

  private val _sendState = MutableStateFlow<Boolean?>(null)
  val sendState: StateFlow<Boolean?>
    get() = _sendState

  fun createUser(email: String, password: String, password2: String) {
    authInteractor.createUser(
      RegistrationBody(
        email = email,
        password = password,
        password2 = password2
      )
    ).onResult {
      Log.d("TAGA", "createUser: $it")
      if (it.isSuccess()) {
        _authState.emit(AuthState.VALID)
      } else {
        if (it.errorValue() == ErrorCode.ALREADY_EXIST.code) {
          _authState.emit(AuthState.USER_EXIST)
        } else if (it.errorValue() == ErrorCode.PASSWORDS_NOT_SAME.code) {
          _authState.emit(AuthState.PASSWORD)
        } else {
          _authState.emit(AuthState.INVALID)
        }

      }
    }.onError {
      _authState.emit(AuthState.INVALID)
    }.launchIn(viewModelScope)
  }

  fun login(email: String, password: String) {
    authInteractor.login(email = email, password = password)
      .onResult {
        if (it.isSuccess()) {
          if (it.dataValue() == true)
            _authState.emit(AuthState.VALID)
          else {
            _authState.emit(AuthState.INVALID)
          }
        } else {
          Log.d("TAGA", "login: ${it.errorValue()} ${ErrorCode.USER_NOT_FOUND.code}")
          if (it.errorValue() == ErrorCode.USER_NOT_FOUND.code) {
            _authState.emit(AuthState.USER_NOT_EXIST)
          } else {
            _authState.emit(AuthState.INVALID)
          }
        }
      }
      .onError {
        Log.d("TAGA", "login: $it")
        _authState.emit(AuthState.INVALID)
      }.launchIn(viewModelScope)
  }

  fun sendOtp(email: String) {
    authInteractor.sendOtp(email)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          if (it.dataValue() == true) {
            _sendState.emit(true)
          } else {
            _sendState.emit(false)
          }
        }
      }.launchIn(viewModelScope)
  }

  fun verifyOtp(email: String, otp: String) {
    authInteractor.verifyOtp(email, otp)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        Log.d("TAGA", "verifyOtp: $it")
        if (it.isSuccess()) {
          if (it.dataValue() == true) {
            _sendState.emit(true)
          } else {
            _sendState.emit(false)
          }
        }
      }.launchIn(viewModelScope)
  }

  fun clearState() {
    _authState.value = AuthState.EMPTY
    _sendState.value = null
  }
}