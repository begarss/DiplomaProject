package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.main.CompanyApi
import kz.kbtu.diplomaproject.data.backend.main.HomeApi
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface CompanyService {
  suspend fun getCompanies(): DataResult<List<Company>?>
  suspend fun searchCompany(name: String?): DataResult<List<Company>?>
  suspend fun getCompanyDetail(id: Int): DataResult<Company?>
  suspend fun makeSubscribe(id: Int): DataResult<Boolean?>
}

class CompanyServiceImpl(private val companyApi: CompanyApi) : CompanyService {
  override suspend fun getCompanies(): DataResult<List<Company>?> = safeCall {
    val response = companyApi.getAllCompanies()
    val body = response.body()
    body
  }

  override suspend fun searchCompany(name: String?): DataResult<List<Company>?> = safeCall {
    val response = companyApi.searchCompany(name)
    val body = response.body()
    body
  }

  override suspend fun getCompanyDetail(id: Int): DataResult<Company?> = safeCall {
    val response = companyApi.getCompanyDetail(id)
    val body = response.body()
    body
  }

  override suspend fun makeSubscribe(id: Int): DataResult<Boolean?> = safeCall {
    val response = companyApi.makeSubscribe(id)
    val body = response.body()
    body
  }

}