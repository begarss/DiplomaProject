package kz.kbtu.diplomaproject.data.backend.main

import kz.kbtu.diplomaproject.data.backend.main.fav.StatusResponse
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CompanyApi {
  @GET("companies")
  suspend fun getAllCompanies(): Response<BaseResponse<List<Company>>>

  @GET("companies_search/")
  suspend fun searchCompany(@Query("search") name: String?): Response<List<Company>?>

  @GET("companies/{id}")
  suspend fun getCompanyDetail(@Path("id") id: Int): Response<Company?>

  @POST("subscribe_pressed/{id}/")
  suspend fun makeSubscribe(@Path("id") id: Int): Response<StatusResponse>

  @GET("companies/{id}/opportunities")
  suspend fun getOppByCompany(@Path("id") id: Int): Response<List<OpportunityDTO>?>
}