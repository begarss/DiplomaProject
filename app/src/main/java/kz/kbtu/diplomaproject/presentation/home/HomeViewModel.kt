package kz.kbtu.diplomaproject.presentation.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.banner.BannerDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel

class HomeViewModel(private val homeInteractor: HomeInteractor) : BaseViewModel() {
  private val TAG = "H0ME_TAGA"
  private val _bannerState = MutableStateFlow<List<BannerDTO>?>(null)
  val bannerState: StateFlow<List<BannerDTO>?> = _bannerState

  private val _postState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val postState: StateFlow<List<OpportunityDTO>?> = _postState

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
      }
      .launchIn(viewModelScope)
  }
}