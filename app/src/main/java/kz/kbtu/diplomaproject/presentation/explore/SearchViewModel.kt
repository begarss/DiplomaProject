package kz.kbtu.diplomaproject.presentation.explore

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterInfo
import kz.kbtu.diplomaproject.presentation.favourites.OppInteractor
import kz.kbtu.diplomaproject.presentation.home.HomeInteractor

class SearchViewModel(
  private val homeInteractor: HomeInteractor,
  private val oppInteractor: OppInteractor
) : BaseViewModel() {

  private val _postState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val postState: StateFlow<List<OpportunityDTO>?> = _postState

  private val _allPostState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val allPostState: StateFlow<List<OpportunityDTO>?> = _allPostState

  fun getOpportunities() {
    homeInteractor.getOpportunities()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onError {
      }
      .onResult {
        Log.d("TAGA", "getOpportunities: $it")
        if (it.isSuccess()) {
          _allPostState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }

  fun applyFilter(filterInfo: FilterInfo) {
    oppInteractor.filterOpportunity(
      category = filterInfo.jobCategory,
      type = filterInfo.jobType,
      contract = filterInfo.contractType,
      company = filterInfo.company,
      title = filterInfo.title
    ).onResult {
      if (it.isSuccess()) {
        _postState.emit(it.dataValue())
      }
    }.launchIn(viewModelScope)
  }
}