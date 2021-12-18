package kz.kbtu.diplomaproject.data.backend.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
  @POST("accounts/register/")
  suspend fun register(@Body body: RegistrationBody): Response<RegistrationResponse?>
}