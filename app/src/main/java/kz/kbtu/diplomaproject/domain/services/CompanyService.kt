package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.main.HomeApi
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface CompanyService {
  suspend fun getCompanies(): DataResult<List<Company>?>
  suspend fun searchCompany(name: String?): DataResult<List<Company>?>
  suspend fun getCompanyDetail(id: Int): DataResult<Company?>
}

class CompanyServiceImpl(private val homeApi: HomeApi) : CompanyService {
  override suspend fun getCompanies(): DataResult<List<Company>?> = safeCall {
    val response = homeApi.getAllCompanies()
    val body = response.body()
    body
  }

  override suspend fun searchCompany(name: String?): DataResult<List<Company>?> = safeCall {
    val response = homeApi.searchCompany(name)
    val body = response.body()
    body
  }

  override suspend fun getCompanyDetail(id: Int): DataResult<Company?> = safeCall {
    val response = homeApi.getCompanyDetail(id)
    val body = response.body()
    body
  }

}