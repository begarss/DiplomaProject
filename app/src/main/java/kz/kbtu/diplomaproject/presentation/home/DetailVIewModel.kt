package kz.kbtu.diplomaproject.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.PostDetail
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.favourites.OppInteractor

class DetailVIewModel(
  private val homeInteractor: HomeInteractor,
  private val oppInteractor: OppInteractor
) : BaseViewModel() {

  private val _detailState = MutableStateFlow<PostDetail?>(null)
  val detailState: StateFlow<PostDetail?> = _detailState

  private val _favState = MutableStateFlow<Boolean?>(null)
  val favState: StateFlow<Boolean?> = _favState

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

  fun addToFavorite(item: PostDetail?) {
    item?.id?.let {
      oppInteractor.addToFav(it)
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