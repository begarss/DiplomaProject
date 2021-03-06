package kz.kbtu.diplomaproject.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.data.backend.main.BannerDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.favourites.OppInteractor

class HomeViewModel(
  private val homeInteractor: HomeInteractor,
  private val oppInteractor: OppInteractor
) : BaseViewModel() {
  private val TAG = "H0ME_TAGA"
  private val _bannerState = MutableStateFlow<List<BannerDTO>?>(null)
  val bannerState: StateFlow<List<BannerDTO>?> = _bannerState

  private val _postState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val postState: StateFlow<List<OpportunityDTO>?> = _postState

  private val _favState = MutableStateFlow<Boolean?>(null)
  val favState: StateFlow<Boolean?> = _favState

  fun getBanners() {
    homeInteractor.getBanners()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          _bannerState.emit(it.dataValue())
        }
      }.launchIn(viewModelScope)
  }

  fun getSubscribedOpperts() {
    homeInteractor.getSubscribedOpports()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onError {
        Log.d(TAG, "getSubscribedOpperts: $it")
      }.onResult {
        _postState.emit(it.dataValue())
        Log.d(TAG, "getSubscribedOpperts: $it")
      }
      .launchIn(viewModelScope)
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

  fun getFavourites() {
    oppInteractor.getFavourites()
      .launchIn(viewModelScope)
  }
}