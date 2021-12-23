package kz.kbtu.diplomaproject.data.backend.banner

import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.PostDetail
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi {
  @GET("addbanner/adds/")
  suspend fun getBanners(): Response<List<BannerDTO>>

  @GET("subscribed_opportunities/")
  suspend fun getSubscribedOppors(): Response<BaseResponse<List<OpportunityDTO>>>

  @GET("opportunities/{id}")
  suspend fun getOpportunityDetail(@Path("id") id: Int): Response<BaseResponse<PostDetail>>
}