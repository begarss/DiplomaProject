package kz.kbtu.diplomaproject.di

import com.google.gson.Gson
import kz.kbtu.diplomaproject.BuildConfig
import kz.kbtu.diplomaproject.data.backend.auth.AuthApi
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {

  single { Gson() }

  single { get<Retrofit>().create(AuthApi::class.java) }

  single {
    Retrofit.Builder()
      .client(get())
      .baseUrl(BuildConfig.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(get()))
      .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
      .build()
  }
}