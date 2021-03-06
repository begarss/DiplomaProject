package kz.kbtu.diplomaproject

import android.app.Application
import com.facebook.stetho.Stetho
import kz.kbtu.diplomaproject.di.baseModule
import kz.kbtu.diplomaproject.di.interactorModule
import kz.kbtu.diplomaproject.di.interceptorModule
import kz.kbtu.diplomaproject.di.okHttpModule
import kz.kbtu.diplomaproject.di.presentationModule
import kz.kbtu.diplomaproject.di.retrofitModule
import kz.kbtu.diplomaproject.di.serviceModule
import kz.kbtu.diplomaproject.di.storageModule
import kz.kbtu.diplomaproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    Stetho.initializeWithDefaults(this)

    startKoin {
      printLogger()
      androidContext(this@App)
      modules(
        presentationModule,
        okHttpModule,
        retrofitModule,
        baseModule,
        interceptorModule,
        serviceModule,
        interactorModule,
        viewModelModule,
        storageModule
      )
    }
  }
}