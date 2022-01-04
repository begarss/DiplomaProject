package kz.kbtu.diplomaproject.data.backend.profile

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApi {
  @GET("api/accounts/users/current/")
  suspend fun getUserInfo(): Response<UserInfo>

  @Multipart
  @PUT("api/accounts/users/current/profile_edit")
  suspend fun setAvatar(@Part filePart: MultipartBody.Part): Response<UserInfo>

  @PUT("api/accounts/users/current/profile_edit")
  suspend fun setUserInfo(@Body userInfo: JsonObject): Response<UserInfo>

}