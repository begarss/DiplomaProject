package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.main.HomeApi
import kz.kbtu.diplomaproject.data.backend.main.BannerDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.PostDetail
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface HomeService {
  suspend fun getBanners(): DataResult<List<BannerDTO>?>
  suspend fun getSubscribedOpports(): DataResult<List<OpportunityDTO>?>
  suspend fun getDetails(id: Int): DataResult<PostDetail?>
  suspend fun getOpportunities(): DataResult<List<OpportunityDTO>?>
}

class HomeServiceImpl(private val homeApi: HomeApi) : HomeService {
  override suspend fun getBanners(): DataResult<List<BannerDTO>?> = safeCall {
    val response = homeApi.getBanners()
    val body = response.body()
    body?.forEach {
      it.image = "http://ithuntt.pythonanywhere.com/${it.image}"
    }
    body
  }

  override suspend fun getSubscribedOpports(): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = homeApi.getSubscribedOppors()
    val body = response.body()?.data
    body?.forEach {
      it.company?.picture = "http://ithuntt.pythonanywhere.com/${it.company?.picture}"
    }
    body
  }

  override suspend fun getDetails(id: Int): DataResult<PostDetail?> = safeCall {
    val response = homeApi.getOpportunityDetail(id)
    val body = response.body()
    body?.data?.company?.apply {
      picture = "http://ithuntt.pythonanywhere.com/${this.picture}"
    }
    body?.data
  }

  override suspend fun getOpportunities(): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = homeApi.getOpportunities()
    response.body()?.data
  }

}