package kz.kbtu.diplomaproject.data.backend.auth

import com.google.gson.JsonObject
import kz.kbtu.diplomaproject.data.common.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
  @POST("api/accounts/register/")
  suspend fun register(@Body body: RegistrationBody): Response<RegistrationResponse?>

  @POST("api/accounts/login/")
  suspend fun login(@Body body: JsonObject): Response<BaseResponse<LoginResponse>>

  @PUT("api/accounts/change-password/")
  suspend fun changePassword(@Body body: JsonObject): Response<BaseResponse<String>>

  @GET("api/accounts/logout/")
  suspend fun logout(): Response<BaseResponse<String>>

  @GET("verify/{email}/")
  suspend fun sendOtp(@Path("email") email: String): Response<JsonObject>

  @POST("verify/{email}/")
  suspend fun verifyOtp(
    @Path("email") email: String,
    @Body otpBody: JsonObject
  ): Response<VerifyResponse>

}