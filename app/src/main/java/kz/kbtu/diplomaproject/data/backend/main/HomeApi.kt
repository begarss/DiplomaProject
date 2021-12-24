package kz.kbtu.diplomaproject.data.backend.main

import kz.kbtu.diplomaproject.data.backend.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.PostDetail
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
  @GET("addbanner/adds/")
  suspend fun getBanners(): Response<List<BannerDTO>>

  @GET("subscribed_opportunities/")
  suspend fun getSubscribedOppors(): Response<BaseResponse<List<OpportunityDTO>>>

  @GET("opportunities/{id}")
  suspend fun getOpportunityDetail(@Path("id") id: Int): Response<BaseResponse<PostDetail>>

  @GET("opportunities")
  suspend fun getOpportunities(): Response<BaseResponse<List<OpportunityDTO>>>

  @GET("companies")
  suspend fun getAllCompanies(): Response<List<Company>?>

  @GET("companies/")
  suspend fun searchCompany(@Query("search") name: String?): Response<List<Company>?>
}