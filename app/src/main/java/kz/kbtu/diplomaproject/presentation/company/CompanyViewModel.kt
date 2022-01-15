package kz.kbtu.diplomaproject.presentation.company

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

class CompanyViewModel(private val companyInteractor: CompanyInteractor) : BaseViewModel() {

  private val _companyState = MutableStateFlow<List<Company>?>(null)
  val companyState: StateFlow<List<Company>?> = _companyState

  private val _companyDetailState = MutableStateFlow<Company?>(null)
  val companyDetailState: StateFlow<Company?> = _companyDetailState

  private val _allCompanyState = MutableStateFlow<List<Company>?>(null)
  val allCompanyState: StateFlow<List<Company>?> = _allCompanyState

  private val _followState = MutableStateFlow<Boolean?>(null)
  val followState: StateFlow<Boolean?> = _followState

  private val _companyOppState = MutableStateFlow<List<OpportunityDTO>?>(null)
  val companyOppState: StateFlow<List<OpportunityDTO>?> = _companyOppState

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

  fun makeSubscribe(id: Int) {
    companyInteractor.makeSubscribe(id)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          _followState.emit(it.dataValue())
        }
      }.launchIn(viewModelScope)
  }

  fun getCompanyDetail(id: Int) {
    companyInteractor.getCompanyDetail(id)
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onResult {
        if (it.isSuccess()) {
          val company = it.dataValue()
          company?.picture = "http://ithuntt.pythonanywhere.com/${company?.picture}"
          _companyDetailState.emit(company)
        }
      }.launchIn(viewModelScope)
  }

  fun clearFollowState() {
    viewModelScope.launch {
      _followState.emit(null)
    }
  }

  fun getOppByCategory(id: Int) {
    companyInteractor.getOppByCompany(id)
      .onResult {
        if (it.isSuccess()) {
          _companyOppState.emit(it.dataValue())
        }
      }.launchIn(viewModelScope)
  }

  fun getSubscribedCompany() {
    companyInteractor.getSubscribedCompanies()
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