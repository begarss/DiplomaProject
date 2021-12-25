package kz.kbtu.diplomaproject.presentation.explore

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.base.MenuItemType
import kz.kbtu.diplomaproject.presentation.base.UserInteractor

class SharedViewModel(private val userInteractor: UserInteractor) : BaseViewModel() {
  private val _authState = MutableStateFlow(false)
  val authState: StateFlow<Boolean> = _authState

  private val _menuSelectionState = MutableSharedFlow<MenuItemType>(1)
  val menuSelectionState: SharedFlow<MenuItemType> = _menuSelectionState

  fun checkTokenExistence() {
    viewModelScope.launch {
      val token = userInteractor.getToken()
      val result = !token.accessToken.isNullOrEmpty()
      _authState.emit(result)
    }
  }

  fun selectMenuItem(item: MenuItemType) {
    viewModelScope.launch {
      _menuSelectionState.emit(item)
    }
  }

}