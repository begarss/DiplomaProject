package kz.kbtu.diplomaproject.domain.services

import kz.airba.infrastructure.helpers.setImageUrl
import kz.kbtu.diplomaproject.data.backend.main.HomeApi
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.mapToEntity
import kz.kbtu.diplomaproject.data.storage.db.dao.FavDao
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface OppService {
  suspend fun addToFav(id: Int, opportunityDTO: OpportunityDTO): DataResult<Boolean?>
  suspend fun getFavourites(): DataResult<List<OpportunityDTO>?>
  suspend fun getAllCategories(): DataResult<List<JobCategory>?>
  suspend fun filterOpportunity(
    category: Int?,
    type: String?,
    contract: String?,
    company: Int?,
    title: String?
  ): DataResult<List<OpportunityDTO>?>
}

class OppServiceImpl(private val homeApi: HomeApi, private val favDao: FavDao) : OppService {
  override suspend fun addToFav(id: Int, opportunityDTO: OpportunityDTO): DataResult<Boolean?> =
    safeCall {
      val response = homeApi.addToFav(id)
      val body = response.body()
      if (body?.status == "success") {
        val localFav = favDao.getFavById(id)
        if (localFav == null) {
          favDao.insertFav(opportunityDTO.toEntity())
        } else {
          favDao.deleteById(id)
        }
      }
      body?.status == "success"
    }

  override suspend fun getFavourites(): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = homeApi.getFavourites()
    val body = response.body()
    if (body?.isSuccessful() == true) {
      favDao.removeAll()
      body.data?.mapToEntity()?.let { favDao.insertFavList(it) }
      body.data?.forEach {
        it.company?.picture = "http://ithuntt.pythonanywhere.com/${it.company?.picture}"
      }
    }
    body?.data
  }

  override suspend fun getAllCategories(): DataResult<List<JobCategory>?> = safeCall {
    val response = homeApi.getAllCategories()
    val body = response.body()
    body?.data
  }

  override suspend fun filterOpportunity(
    category: Int?,
    type: String?,
    contract: String?,
    company: Int?,
    title: String?
  ): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = homeApi.filterOpportunity(title, category, type, contract, company)
    val body = response.body()
    val favs = favDao.getAllFavs()
    val favId = favs.map {
      it.id
    }
    body?.forEach {
      if (favId.contains(it.id)) {
        it.isFavourate = true
      }
//      setImageUrl(it)
    }
    body
  }

}