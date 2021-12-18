package kz.kbtu.diplomaproject.data.backend.banner

import retrofit2.Response
import retrofit2.http.GET

interface HomeApi {
  @GET("addbanner/adds/")
  suspend fun getBanners(): Response<List<BannerDTO>>
}