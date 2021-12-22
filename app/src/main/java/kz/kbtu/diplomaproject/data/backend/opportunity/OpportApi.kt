package kz.kbtu.diplomaproject.data.backend.opportunity

import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface OpportApi {
  @GET("subscribed_opportunities")
  suspend fun getSubscribedOppors(): Response<BaseResponse<List<OpportunityDTO>>>
}