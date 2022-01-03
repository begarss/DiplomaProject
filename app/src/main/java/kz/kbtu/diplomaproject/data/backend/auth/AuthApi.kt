package kz.kbtu.diplomaproject.data.backend.auth

import com.google.gson.JsonObject
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
  @POST("accounts/register/")
  suspend fun register(@Body body: RegistrationBody): Response<RegistrationResponse?>

  @POST("accounts/login/")
  suspend fun login(@Body body: JsonObject): Response<BaseResponse<LoginResponse>>

  @PUT("accounts/change-password/")
  suspend fun changePassword(@Body body: JsonObject): Response<BaseResponse<String>>

  @GET("accounts/logout/")
  suspend fun logout(): Response<BaseResponse<String>>
}