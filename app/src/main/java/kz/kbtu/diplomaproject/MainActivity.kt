package kz.kbtu.diplomaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentContainerView
import kz.kbtu.diplomaproject.presentation.MainFragment
import kz.kbtu.diplomaproject.presentation.containers.AuthorizationFragment

class MainActivity : AppCompatActivity() {
  private lateinit var container: FragmentContainerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    setContentView(R.layout.activity_main)
    container = findViewById(R.id.container_fragment)
    openAuthorizationContainer()
  }

  fun openAuthorizationContainer() {
    supportFragmentManager.beginTransaction()
      .replace(R.id.container_fragment, AuthorizationFragment())
      .commit()
  }
  fun openMainContainer() {
    supportFragmentManager.beginTransaction()
      .replace(R.id.container_fragment, MainFragment())
      .addToBackStack(null)
      .commit()
  }
}
