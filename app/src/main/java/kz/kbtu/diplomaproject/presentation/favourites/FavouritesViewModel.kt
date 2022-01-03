package kz.kbtu.diplomaproject.presentation.favourites

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel

class FavouritesViewModel(private val oppInteractor: OppInteractor) : BaseViewModel() {

  private val _favState = MutableStateFlow<Boolean?>(null)
  val favState: StateFlow<Boolean?> = _favState

  private val _postState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val postState: StateFlow<List<OpportunityDTO>?> = _postState

  fun getFavourites() {
    oppInteractor.getFavourites()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
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