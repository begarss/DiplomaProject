package kz.kbtu.diplomaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kz.kbtu.diplomaproject.presentation.MainFragment
import kz.kbtu.diplomaproject.presentation.containers.AuthorizationFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
  private lateinit var container: FragmentContainerView
  private val sharedViewModel: SharedViewModel by viewModel()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    setContentView(R.layout.activity_main)
    sharedViewModel.checkTokenExistence()
    container = findViewById(R.id.container_fragment)
    observeToken()
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

  private fun observeToken(){
    lifecycleScope.launchWhenCreated {
      sharedViewModel.authState.collect {
        if (it){
          openMainContainer()
        }else{
          openAuthorizationContainer()
        }
      }
    }
  }
}
