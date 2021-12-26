package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.main.HomeApi
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface OppService {
  suspend fun addToFav(id: Int): DataResult<Boolean?>
  suspend fun getFavourites(): DataResult<List<OpportunityDTO>?>
  suspend fun getAllCategories(): DataResult<List<JobCategory>?>
}

class OppServiceImpl(private val homeApi: HomeApi) : OppService {
  override suspend fun addToFav(id: Int): DataResult<Boolean?> = safeCall {
    val response = homeApi.addToFav(id)
    val body = response.body()
    body?.status == "success"
  }

  override suspend fun getFavourites(): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = homeApi.getFavourites()
    val body = response.body()
    body?.data
  }

  override suspend fun getAllCategories(): DataResult<List<JobCategory>?> = safeCall {
    val response = homeApi.getAllCategories()
    val body = response.body()
    body?.data
  }

}