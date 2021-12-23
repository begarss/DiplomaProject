package kz.kbtu.diplomaproject.domain.services

import android.util.Log
import com.google.gson.JsonObject
import kz.kbtu.diplomaproject.data.backend.profile.ProfileApi
import kz.kbtu.diplomaproject.data.backend.profile.UserInfo
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part

interface ProfileService {
  suspend fun getUserInfo(): DataResult<UserInfo?>
  suspend fun setUserImage(file: MultipartBody.Part): DataResult<UserInfo?>
  suspend fun setUserInfo(
    userName: String?,
    birthDay: String?,
    phone: String?
  ): DataResult<UserInfo?>
}

class ProfileServiceImpl(private val profileApi: ProfileApi) : ProfileService {
  override suspend fun getUserInfo(): DataResult<UserInfo?> = safeCall {
    val response = profileApi.getUserInfo()
    val body = response.body()
    body
  }

  override suspend fun setUserImage(file: Part): DataResult<UserInfo?> = safeCall {
    val response = profileApi.setAvatar(filePart = file)
    val body = response.body()
    Log.d("TAGA", "setUserImage: $response")
    body
  }

  override suspend fun setUserInfo(
    userName: String?,
    birthDay: String?,
    phone: String?
  ): DataResult<UserInfo?> =
    safeCall {
      val userInfo = JsonObject().apply {
        addProperty("first_name", userName)
        addProperty("birthday", birthDay)
        addProperty("contact_number", phone)
      }
      val response = profileApi.setUserInfo(userInfo = userInfo)
      val body = response.body()
      body
    }

}