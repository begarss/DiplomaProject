package kz.kbtu.diplomaproject.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.setGone
import kz.airba.infrastructure.helpers.setVisible
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentMainBinding
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.COMPANY
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.EXPLORE
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.FAV
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.HOME
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.PROFILE
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment() {
  private lateinit var binding: FragmentMainBinding
  override val viewModel: SharedViewModel by sharedStateViewModel()

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
    binding.bottomNavigationView.itemIconTintList = null
    observeMenu()
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

  private fun observeMenu() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.menuSelectionState.collect { item ->
        val result = when (item) {
          HOME -> R.id.homeFragment
          FAV -> R.id.favouriteFragment
          EXPLORE -> R.id.exploreFragment
          COMPANY -> R.id.companyFragment
          PROFILE -> R.id.profileFragment
        }
        binding.bottomNavigationView.selectedItemId = result
      }
    }
  }
}