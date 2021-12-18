package kz.kbtu.diplomaproject.data.storage

import android.content.Context
import com.google.gson.Gson
import kz.kbtu.diplomaproject.data.common.TokenInfo

interface Preferences {

  fun saveTokenInfo(tokenInfo: TokenInfo)
  fun getTokenInfo(): TokenInfo
  fun clearToken()

  fun clearData()
  fun skipAuthorization()
  fun isAuthorizationSkipped(): Boolean
}

class PreferencesImpl(
  context: Context,
  private val gson: Gson
) : Preferences {

  private val sharedPref = context.getSharedPreferences(
    "kz.kbtu.preference_file_key", Context.MODE_PRIVATE
  )

  override fun clearToken() = sharedPref.edit()
    .remove(tokenKey)
    .apply()

  override fun saveTokenInfo(tokenInfo: TokenInfo) = sharedPref.edit()
    .putString(tokenKey, gson.toJson(tokenInfo))
    .apply()

  override fun getTokenInfo(): TokenInfo =
    gson.fromJson(sharedPref.getString(tokenKey, "{}"), TokenInfo::class.java)

  override fun skipAuthorization() = sharedPref.edit()
    .putBoolean(skipAuthorizationKey, true)
    .apply()

  override fun isAuthorizationSkipped() = sharedPref.getBoolean(skipAuthorizationKey, false)

  override fun clearData() {
    sharedPref.edit()
      .clear()
      .apply()
  }

  companion object {
    private const val prefix = "kz.kbtu."
    private const val tokenKey = "${prefix}token"
    private const val userKey = "${prefix}user"
    private const val skipAuthorizationKey = "${prefix}skipAuthorization"
    private const val uuidKey = "${prefix}uuid"
    private const val addressKey = "${prefix}.address"
  }
}


