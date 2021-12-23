package kz.kbtu.diplomaproject.data.backend.profile

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApi {
  @GET("accounts/users/current/")
  suspend fun getUserInfo(): Response<UserInfo>

  @Multipart
  @PUT("accounts/users/current/profile_edit")
  suspend fun setAvatar(@Part filePart: MultipartBody.Part): Response<UserInfo>

}