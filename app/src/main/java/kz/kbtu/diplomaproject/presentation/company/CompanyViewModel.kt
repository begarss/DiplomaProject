package kz.kbtu.diplomaproject.presentation.company

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel

class CompanyViewModel(private val companyInteractor: CompanyInteractor) : BaseViewModel() {

  private val _companyState = MutableStateFlow<List<Company>?>(null)
  val companyState: StateFlow<List<Company>?> = _companyState

  private val _allCompanyState = MutableStateFlow<List<Company>?>(null)
  val allCompanyState: StateFlow<List<Company>?> = _allCompanyState

  fun getCompanies() {
    companyInteractor.getCompanies()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onError {
      }
      .onResult {
        if (it.isSuccess()) {
          _allCompanyState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }

  fun searchCompany(name: String?) {
    companyInteractor.searchCompany(name)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          _companyState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }


}