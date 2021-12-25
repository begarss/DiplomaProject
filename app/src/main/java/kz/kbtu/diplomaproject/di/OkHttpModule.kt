package kz.kbtu.diplomaproject.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import kz.kbtu.diplomaproject.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val okHttpModule = module {

  val cacheSize: Long = 20 * 1024 * 1024 //20mb

  single {
    Cache(androidContext().cacheDir, cacheSize)
  }

  single {
    OkHttpClient.Builder()
      .connectTimeout(120, TimeUnit.SECONDS)
      .writeTimeout(120, TimeUnit.SECONDS)
      .readTimeout(120, TimeUnit.SECONDS)
      .cookieJar(
        PersistentCookieJar(
          SetCookieCache(),
          SharedPrefsCookiePersistor(androidContext())
        )
      )
      .addNetworkInterceptor(StethoInterceptor())
      .addInterceptor(get<MainInterceptor>())
      .addInterceptor(provideLoggingInterceptor())
      .cache(get())
      .build()
  }
}

internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
  val interceptor = HttpLoggingInterceptor()
  interceptor.level = if (BuildConfig.DEBUG)
    HttpLoggingInterceptor.Level.BODY
  else
    HttpLoggingInterceptor.Level.NONE
  return interceptor
}