package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.banner.HomeApi
import kz.kbtu.diplomaproject.data.backend.banner.BannerDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.PostDetail
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface HomeService {
  suspend fun getBanners(): DataResult<List<BannerDTO>?>
  suspend fun getSubscribedOpports(): DataResult<List<OpportunityDTO>?>
  suspend fun getDetails(id: Int): DataResult<PostDetail?>
}

class HomeServiceImpl(private val homeApi: HomeApi) : HomeService {
  override suspend fun getBanners(): DataResult<List<BannerDTO>?> = safeCall {
    val response = homeApi.getBanners()
    val body = response.body()
    body?.forEach {
      it.image = "http://ithunt.pythonanywhere.com/${it.image}"
    }
    body
  }

  override suspend fun getSubscribedOpports(): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = homeApi.getSubscribedOppors()
    response.body()?.data
  }

  override suspend fun getDetails(id: Int): DataResult<PostDetail?> = safeCall {
    val response = homeApi.getOpportunityDetail(id)
    val body = response.body()
    body?.data
  }

}