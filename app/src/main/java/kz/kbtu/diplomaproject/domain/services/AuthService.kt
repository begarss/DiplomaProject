package kz.kbtu.diplomaproject.domain.services

import android.util.Log
import com.google.gson.JsonObject
import kz.kbtu.diplomaproject.data.backend.auth.AuthApi
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationBody
import kz.kbtu.diplomaproject.data.common.TokenInfo
import kz.kbtu.diplomaproject.data.storage.Preferences
import kz.kbtu.diplomaproject.data.storage.db.dao.FavDao
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface AuthService {
  suspend fun register(request: RegistrationBody): DataResult<Boolean>
  suspend fun login(email: String, password: String): DataResult<Boolean>
  suspend fun changePassword(oldPassword: String, newPassword: String): DataResult<Boolean>
  suspend fun logout(): DataResult<Boolean>
  fun clearUserData()
  suspend fun sendOtp(email: String): DataResult<Boolean>
  suspend fun verifyOtp(email: String, otp: String): DataResult<Boolean>
}

class AuthServiceImpl(
  private val authApi: AuthApi,
  private val preferences: Preferences,
  private val favDao: FavDao
) : AuthService {
  override suspend fun register(request: RegistrationBody): DataResult<Boolean> =
    try {
      val response = authApi.register(body = request)
      Log.d("TAGA", "register: ${response.code()}")
      if (response.isSuccessful) {
        val body = response.body()
        if (!body?.token.isNullOrEmpty()) {
          preferences.saveTokenInfo(TokenInfo(accessToken = body?.token, ""))
          DataResult.Success(true)
        } else {
          DataResult.Success(false)
        }
      } else {
        DataResult.Error("Server error")
      }
    } catch (e: Throwable) {
      DataResult.Error(e.localizedMessage)
    }

  override suspend fun login(email: String, password: String): DataResult<Boolean> =
    try {
      val jsonBody = JsonObject().also {
        it.addProperty("email", email)
        it.addProperty("password", password)
      }
      val response = authApi.login(body = jsonBody)
      if (response.isSuccessful) {
        response.headers()["Set-Cookie"]
        val sessionId = response.headers().values("Set-Cookie")[0].split(";")[0].split("=")[1]
        val body = response.body()
        if (body?.isError() == false) {
          Log.d(
            "TAGA", "login: $sessionId  ${
              response.headers()["Set-Cookie"]
            }"
          )
          preferences.saveTokenInfo(TokenInfo(accessToken = body.data.token, sessionId = null))
          DataResult.Success(true)
        } else {
          DataResult.Error(body?.error)
        }
      } else {
//        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
//        DataResult.Error(jsonObj.getString("error"))
        DataResult.Error(response.body()?.error)
      }
    } catch (e: Throwable) {
      DataResult.Error(e.localizedMessage)
    }

  override suspend fun changePassword(
    oldPassword: String,
    newPassword: String
  ): DataResult<Boolean> =
    try {
      val bodyObject = JsonObject().apply {
        addProperty("oldPassword", oldPassword)
        addProperty("newPassword", newPassword)
      }
      val response = authApi.changePassword(body = bodyObject)

      if (response.isSuccessful) {
        val body = response.body()
        if (body?.isError() == false) {
          DataResult.Success(true)
        } else {
          DataResult.Error(body?.error)
        }

      } else {
        DataResult.Error(response.message())
      }

    } catch (e: Throwable) {
      DataResult.Error(e.localizedMessage)
    }

  override suspend fun logout(): DataResult<Boolean> =
    try {
      val response = authApi.logout()
      if (response.isSuccessful) {
        val body = response.body()
        if (body?.isError() == false) {
          clearUserData()
          DataResult.Success(true)
        } else {
          DataResult.Error(body?.error)
        }
      } else {
        DataResult.Error(response.message())
      }

    } catch (e: Throwable) {
      DataResult.Error(e.localizedMessage)
    }

  override fun clearUserData() {
    preferences.clearToken()
    favDao.removeAll()
  }

  override suspend fun sendOtp(email: String): DataResult<Boolean> = safeCall {
    val response = authApi.sendOtp(email)
    val body = response.body()
//    val res = body?.get("OTP")
    body?.isJsonNull != true
  }

  override suspend fun verifyOtp(email: String, otp: String): DataResult<Boolean> = safeCall {
    val otpBody = JsonObject().apply {
      addProperty("otp", otp)
    }
    val response = authApi.verifyOtp(email = email, otpBody = otpBody)
    val body = response.body()
    if (body != null) {
      preferences.saveTokenInfo(TokenInfo(accessToken = body.token, null))
    }
    return@safeCall body != null
  }

}