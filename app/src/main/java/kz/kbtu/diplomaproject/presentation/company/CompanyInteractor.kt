package kz.kbtu.diplomaproject.presentation.company

import kz.kbtu.diplomaproject.data.backend.opportunity.Company
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.CompanyService

interface CompanyInteractor {
  fun getCompanies(): Async<DataResult<List<Company>?>>
}

class CompanyInteractorImpl(private val companyService: CompanyService) : CompanyInteractor,
  CoroutineInteractor {
  override fun getCompanies() = async {
    companyService.getCompanies()
  }

}