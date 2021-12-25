package kz.kbtu.diplomaproject.presentation.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.opportunity.PostDetail
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel

class DetailVIewModel(private val homeInteractor: HomeInteractor) : BaseViewModel() {

  private val _detailState = MutableStateFlow<PostDetail?>(null)
  val detailState: StateFlow<PostDetail?> = _detailState

  fun getDetails(id: Int) {
    homeInteractor.getDetails(id)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          _detailState.emit(
            it
              .dataValue()
          )
        }
      }.launchIn(viewModelScope)
  }
}