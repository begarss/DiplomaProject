package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.BuildConfig
import kz.kbtu.diplomaproject.data.common.Headers
import kz.kbtu.diplomaproject.data.storage.Preferences
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MainInterceptor(private val preferences: Preferences) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val newRequest: Request
    val tokenInfo = preferences.getTokenInfo()
    val builder = request.newBuilder().apply {
      if (!tokenInfo.accessToken.isNullOrEmpty()) {
        header(Headers.AUTHORIZATION, "Token ${tokenInfo.accessToken}")
      }
      if (!tokenInfo.sessionId.isNullOrEmpty()) {
        header(Headers.COOKIE, "sessionid ${tokenInfo.sessionId}")
      }
      header(
        Headers.USER_AGENT,
        "build:${BuildConfig.VERSION_CODE}; Android ${android.os.Build.VERSION.SDK_INT})"
      )
    }

    newRequest = builder.build()
    return chain.proceed(newRequest).newBuilder().build()
  }
}