package kz.kbtu.diplomaproject.presentation.explore

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.home.HomeInteractor

class SearchViewModel(private val homeInteractor: HomeInteractor) : BaseViewModel() {

  private val _postState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val postState: StateFlow<List<OpportunityDTO>?> = _postState

  fun getOpportunities() {
    homeInteractor.getOpportunities()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onError {
      }
      .onResult {
        if (it.isSuccess()) {
          _postState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }
}