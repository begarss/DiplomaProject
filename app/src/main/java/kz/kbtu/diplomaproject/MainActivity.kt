package kz.kbtu.diplomaproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainerView
import kz.kbtu.diplomaproject.presentation.MainFragment

class MainActivity : AppCompatActivity() {
  private lateinit var container: FragmentContainerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    container = findViewById(R.id.container_fragment)
    setContentView(R.layout.activity_main)
    openMainContainer()
  }

  fun openMainContainer() {
    supportFragmentManager.beginTransaction()
      .replace(R.id.container_fragment, MainFragment())
      .addToBackStack(null)
      .commit()
  }
}
