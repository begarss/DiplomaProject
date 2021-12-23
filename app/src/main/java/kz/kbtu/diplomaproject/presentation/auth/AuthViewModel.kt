package kz.kbtu.diplomaproject.presentation.auth

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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
        _authState.emit(AuthState.INVALID)
      }
    }.onError {
      _authState.emit(AuthState.INVALID)
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
    _authState.value = AuthState.EMPTY
  }
}