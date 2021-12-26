package kz.kbtu.diplomaproject.presentation.explore.filter

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.mapToModel
import kz.kbtu.diplomaproject.domain.helpers.operators.launchIn
import kz.kbtu.diplomaproject.domain.helpers.operators.onCompletion
import kz.kbtu.diplomaproject.domain.helpers.operators.onConsume
import kz.kbtu.diplomaproject.domain.helpers.operators.onError
import kz.kbtu.diplomaproject.domain.helpers.operators.onResult
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.company.CompanyInteractor
import kz.kbtu.diplomaproject.presentation.favourites.OppInteractor

class FilterViewModel(
  private val oppInteractor: OppInteractor,
  private val companyInteractor: CompanyInteractor
) : BaseViewModel() {

  private val _categoryState = MutableStateFlow<List<JobCategory>?>(null)
  val categoryState: StateFlow<List<JobCategory>?> = _categoryState

  private val _companyState = MutableStateFlow<List<CompanyModel>?>(null)
  val companyState: StateFlow<List<CompanyModel>?> = _companyState

  fun getCompanies() {
    companyInteractor.getCompanies()
      .onConsume { showLoader() }
      .onCompletion { hideLoader() }
      .onError {
      }
      .onResult {
        if (it.isSuccess()) {
          val data = it.dataValue()?.mapToModel()
          _companyState.emit(data)
        }
      }
      .launchIn(viewModelScope)
  }

  fun getAllCategories() {
    oppInteractor.getCategories()
      .onResult {
        if (it.isSuccess()) {
          _categoryState.emit(it.dataValue())
        }
      }
      .launchIn(viewModelScope)
  }
}