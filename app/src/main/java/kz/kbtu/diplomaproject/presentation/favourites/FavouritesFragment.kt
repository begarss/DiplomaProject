package kz.kbtu.diplomaproject.presentation.favourites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.airba.infrastructure.helpers.navigateSafely
import kz.airba.infrastructure.helpers.show
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentFavouritesBinding
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.EXPLORE
import kz.kbtu.diplomaproject.presentation.home.PostAdapter
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : BaseFragment() {
  override val viewModel: FavouritesViewModel by viewModel()
  private val sharedViewModel: SharedViewModel by sharedStateViewModel()

  private lateinit var binding: FragmentFavouritesBinding

  private val postAdapter by lazy {
    PostAdapter(arrayListOf(), onFavClick = {
      viewModel.addToFavorite(it)
    }, onItemClick = {
      navigateSafely(FavouritesFragmentDirections.actionFavouriteFragmentToPostDetailFragment(it))
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentFavouritesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getFavourites()
    bindView()
    observeOpports()
    observeFavState()
  }

  private fun bindView() {
    with(binding) {
      mainRV.initRecyclerView()
      mainRV.adapter = postAdapter

      toolbar.toolbar.apply {
        title = "Favourite opportunities"
        navigationIcon = null
      }

      emptyView.btnOpenSearch.setOnClickListener {
        sharedViewModel.selectMenuItem(EXPLORE)
      }
    }
  }

  private fun observeOpports() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.postState.collect {
        if (it?.isEmpty() == true) {
          binding.emptyView.root.show()
          binding.favContainer.hide()
        } else {
          it?.let {
            postAdapter.addAll(it)
          }
          binding.emptyView.root.hide()
          binding.favContainer.show()
        }
      }
    }
  }

  private fun observeFavState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.favState.collect {
        Log.d("TAGA", "hjghjgjhghjghj: $it")
        if (it == true) {
          viewModel.getFavourites()
          viewModel.clearFavState()
        }
      }
    }
  }
}