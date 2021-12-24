package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.main.HomeApi
import kz.kbtu.diplomaproject.data.backend.opportunity.Company
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface CompanyService {
  suspend fun getCompanies(): DataResult<List<Company>?>
}

class CompanyServiceImpl(private val homeApi: HomeApi) : CompanyService {
  override suspend fun getCompanies(): DataResult<List<Company>?> = safeCall {
    val response = homeApi.getAllCompanies()
    val body = response.body()
    body
  }

}