package kz.kbtu.diplomaproject.data.backend.profile

import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
  @GET("accounts/users/current/")
  suspend fun getUserInfo(): Response<UserInfo>
}