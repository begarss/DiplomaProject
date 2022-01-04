package kz.kbtu.diplomaproject.data.backend.main

import kz.kbtu.diplomaproject.data.backend.main.fav.StatusResponse
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.PostDetail
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeApi {
  @GET("api/addbanner/adds/")
  suspend fun getBanners(): Response<List<BannerDTO>>

  @GET("api/subscribed_opportunities/")
  suspend fun getSubscribedOppors(): Response<BaseResponse<List<OpportunityDTO>>>

  @GET("api/opportunities/{id}")
  suspend fun getOpportunityDetail(@Path("id") id: Int): Response<BaseResponse<PostDetail>>

  @GET("api/opportunities")
  suspend fun getOpportunities(): Response<BaseResponse<List<OpportunityDTO>?>>

  @GET("api/favourate_opportunities/")
  suspend fun getFavourites(): Response<BaseResponse<List<OpportunityDTO>?>>

  @POST("api/favourate_pressed/{id}/")
  suspend fun addToFav(
    @Path("id") id: Int
  ): Response<StatusResponse>

  @GET("api/job_categories")
  suspend fun getAllCategories(): Response<BaseResponse<List<JobCategory>?>>

  @GET("api/opportunities_company/")
  suspend fun filterOpportunity(
    @Query("title") title: String? = null,
    @Query("job_category") category: Int? = null,
    @Query("job_type") type: String? = null,
    @Query("contract_type") contract: String? = null,
    @Query("company") company: Int? = null,
  ): Response<List<OpportunityDTO>?>

//  @GET("opportunities/")
//  suspend fun filterOpportunity(
//    @QueryMap options: Map<String, String>
//  ): Response<List<OpportunityDTO>?>
}