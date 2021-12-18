package kz.kbtu.diplomaproject.domain.services

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import kz.kbtu.diplomaproject.data.backend.auth.AuthApi
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationBody
import kz.kbtu.diplomaproject.data.backend.auth.RegistrationResponse
import kz.kbtu.diplomaproject.data.common.TokenInfo
import kz.kbtu.diplomaproject.data.storage.Preferences
import kz.kbtu.diplomaproject.domain.model.DataResult
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

interface AuthService {
  suspend fun register(request: RegistrationBody): DataResult<Boolean>
}

class AuthServiceImpl(
  private val authApi: AuthApi,
  private val preferences: Preferences
) : AuthService {
  override suspend fun register(request: RegistrationBody): DataResult<Boolean> =
    try {
      val response = authApi.register(body = request)
      Log.d("TAGA", "register: ${response.code()}")
      if (response.isSuccessful) {
        val body = response.body()
        if (!body?.token.isNullOrEmpty()) {
          preferences.saveTokenInfo(TokenInfo(accessToken = body?.token))
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

}