package kz.kbtu.diplomaproject.presentation.explore

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

  private val _favState = MutableStateFlow<Boolean?>(null)
  val favState: StateFlow<Boolean?> = _favState

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
  fun addToFavorite(item: OpportunityDTO?) {
    item?.id?.let {
      oppInteractor.addToFav(it, item)
        .onError { Log.d("TAGA", "addToFavorite: $it") }
        .onResult { result ->
          Log.d("TAGA", "addToFavorite: $it")
          if (result.dataValue() == true) {
            _favState.emit(true)
          }
        }
        .onConsume { showLoader() }
        .onCompletion { hideLoader() }
        .launchIn(viewModelScope)
    }
  }

  fun clearFavState() {
    viewModelScope.launch {
      _favState.emit(null)
    }
  }
}