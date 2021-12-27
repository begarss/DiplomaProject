package kz.kbtu.diplomaproject.data.backend.main

import kz.kbtu.diplomaproject.data.backend.main.fav.StatusResponse
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.PostDetail
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface HomeApi {
  @GET("addbanner/adds/")
  suspend fun getBanners(): Response<List<BannerDTO>>

  @GET("subscribed_opportunities/")
  suspend fun getSubscribedOppors(): Response<BaseResponse<List<OpportunityDTO>>>

  @GET("opportunities/{id}")
  suspend fun getOpportunityDetail(@Path("id") id: Int): Response<BaseResponse<PostDetail>>

  @GET("opportunities")
  suspend fun getOpportunities(): Response<List<OpportunityDTO>?>

  @GET("companies")
  suspend fun getAllCompanies(): Response<List<Company>?>

  @GET("companies/")
  suspend fun searchCompany(@Query("search") name: String?): Response<List<Company>?>

  @GET("companies/{id}")
  suspend fun getCompanyDetail(@Path("id") id: Int): Response<Company?>

  @GET("favourate_opportunities/")
  suspend fun getFavourites(): Response<BaseResponse<List<OpportunityDTO>?>>

  @POST("favourate_pressed/{id}/")
  suspend fun addToFav(@Path("id") id: Int): Response<StatusResponse>

  @GET("job_categories")
  suspend fun getAllCategories(): Response<BaseResponse<List<JobCategory>?>>

    @GET("opportunities/")
  suspend fun filterOpportunity(
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