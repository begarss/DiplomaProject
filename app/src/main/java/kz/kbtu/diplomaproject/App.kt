package kz.kbtu.diplomaproject

import android.app.Application
import kz.kbtu.diplomaproject.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      printLogger()
      androidContext(this@App)
      modules(presentationModule)
    }
  }
}