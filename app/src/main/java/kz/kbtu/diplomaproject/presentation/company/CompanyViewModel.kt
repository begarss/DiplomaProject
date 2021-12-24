package kz.kbtu.diplomaproject.presentation.company

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel

class CompanyViewModel(private val companyInteractor: CompanyInteractor) : BaseViewModel() {

  private val _companyState = MutableStateFlow<List<Company>?>(null)
  val companyState: StateFlow<List<Company>?> = _companyState

  fun getCompanies() {
    companyInteractor.getCompanies()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onError {
      }
      .onResult {
        if (it.isSuccess()) {
          _companyState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }
}