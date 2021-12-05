package kz.kbtu.diplomaproject.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kz.airba.infrastructure.helpers.setGone
import kz.airba.infrastructure.helpers.setVisible
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {
  private lateinit var binding: FragmentMainBinding
  override val viewModel: SharedViewModel by viewModel()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.mainHostContainer.getFragment<NavHostFragment>().navController.run {
      binding.bottomNavigationView.setupWithNavController(this)
      addOnDestinationChangedListener { _, destination, _ ->
        when (destination.id) {
          R.id.homeFragment, R.id.favouriteFragment, R.id.exploreFragment, R.id.profileFragment -> showBottomNavigation()
          else -> hideBottomNavigation()
        }
      }
    }
    backPress()
  }

  private fun hideBottomNavigation() {
    binding.bottomNavigationView.setGone()
    binding.separator.setGone()
  }

  private fun showBottomNavigation() {
    binding.bottomNavigationView.setVisible()
    binding.separator.setVisible()
  }

  private fun backPress() {
    activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          activity?.finish()
        }

      })
  }

}