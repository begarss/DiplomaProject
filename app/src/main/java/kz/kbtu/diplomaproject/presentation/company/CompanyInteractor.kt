package kz.kbtu.diplomaproject.presentation.company

import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.CompanyService

interface CompanyInteractor {
  fun getCompanies(): Async<DataResult<List<Company>?>>
  fun searchCompany(name: String?): Async<DataResult<List<Company>?>>
  fun getCompanyDetail(id: Int): Async<DataResult<Company?>>
  fun makeSubscribe(id: Int): Async<DataResult<Boolean?>>
  fun getOppByCompany(id: Int): Async<DataResult<List<OpportunityDTO>?>>
  fun getSubscribedCompanies(): Async<DataResult<List<Company>?>>
}

class CompanyInteractorImpl(private val companyService: CompanyService) : CompanyInteractor,
  CoroutineInteractor {
  override fun getCompanies() = async {
    companyService.getCompanies()
  }

  override fun searchCompany(name: String?) = async {
    companyService.searchCompany(name)
  }

  override fun getCompanyDetail(id: Int) = async {
    companyService.getCompanyDetail(id)
  }

  override fun makeSubscribe(id: Int) = async {
    companyService.makeSubscribe(id)
  }

  override fun getOppByCompany(id: Int) = async {
    companyService.getOppByCompany(id)
  }

  override fun getSubscribedCompanies() = async { companyService.getSubscribedCompanies() }

}