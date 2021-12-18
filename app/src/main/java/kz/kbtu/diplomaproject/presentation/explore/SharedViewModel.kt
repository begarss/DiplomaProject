package kz.kbtu.diplomaproject.presentation.explore

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.base.UserInteractor

class SharedViewModel(private val userInteractor: UserInteractor) : BaseViewModel() {
  private val _authState = MutableStateFlow(false)
  val authState: StateFlow<Boolean> = _authState

  fun checkTokenExistence() {
    viewModelScope.launch {
      val token = userInteractor.getToken()
      val result = !token.accessToken.isNullOrEmpty()
      _authState.emit(result)
    }
  }
}