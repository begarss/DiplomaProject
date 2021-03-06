package kz.kbtu.diplomaproject.presentation.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.focusAndShowKeyboard
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.airba.infrastructure.helpers.navigateSafely
import kz.airba.infrastructure.helpers.show
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentExploreBinding
import kz.kbtu.diplomaproject.domain.helpers.operators.debounce
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.filter.FilterFragment
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ChipIds
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterInfo
import kz.kbtu.diplomaproject.presentation.home.HomeFragmentDirections
import kz.kbtu.diplomaproject.presentation.home.PostAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Arrays

class ExploreFragment : BaseFragment() {
  override val viewModel: SearchViewModel by viewModel()
  private lateinit var binding: FragmentExploreBinding
  private var filterData: FilterInfo? = null

  private val adapter by lazy {
    PostAdapter(arrayListOf(), onFavClick = {
      viewModel.addToFavorite(it)

    }, onItemClick = {
      navigateSafely(ExploreFragmentDirections.actionExploreFragmentToPostDetailFragment(it))

    })
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setFragmentResultListener(requestKey = FilterFragment.FILTER_KEY, listener = { _, bundle ->
      filterData = bundle.getParcelable<FilterInfo>(FilterFragment.FILTER_DATA_RESULT)
      if (filterData != null) {
        observeFilters(filterData)
        Log.d("TAGA", "onCreate: $filterData")
      }
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    if (filterData == null) {
      viewModel.getOpportunities()
      observeAllPosts()
    }
    setUpSearchView()
    observeFavState()
  }

  private fun bindViews() {
    with(binding) {
      searchFragmentToolbar.inflateMenu(R.menu.search_menu)
      searchFragmentToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

      rvSearchResult.initRecyclerView()
      rvSearchResult.adapter = adapter

      fabFilter.setOnClickListener {
        Log.d("TAGA", "explore to bindViews: ${filterData?.savedChips}")
        val bundle = Bundle().apply {
          filterData?.savedChips?.let {

            putParcelableArrayList(FilterFragment.STORED_CHIPS, it)
          }
        }

        navigateSafely(
          R.id.action_exploreFragment_to_filterFragment,
          bundle
        )
      }
    }
  }

  private fun setNewFilterDataWithTitle(title: String) {
    filterData =
      FilterInfo(
        title = title,
        null,
        null,
        null,
        null,
        null
      )
    viewModel.applyFilter(filterData!!)
  }

  private fun setUpSearchView() {
    val onQueryChanged = lifecycleScope.debounce<String> {
      if (it.length >= 3) {
        filterData?.let { filterInfo ->
          filterInfo.title = it
          viewModel.applyFilter(filterInfo)
        } ?: setNewFilterDataWithTitle(it)
      }
    }

    val searchMenuItem = binding.searchFragmentToolbar.menu.findItem(R.id.action_search)
    val searchView = searchMenuItem.actionView as SearchView

    searchView.apply {
      setIconifiedByDefault(false)
      onActionViewExpanded()
      val searchIcon: ImageView =
        searchView.findViewById(R.id.search_mag_icon)
      searchIcon.setImageDrawable(null)
      val v: View = searchView.findViewById(R.id.search_plate)
      searchView.queryHint = "Search Opportunity"
      v.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
      setOnQueryTextListener(object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = false

        override fun onQueryTextChange(newText: String?): Boolean {
          newText?.let(onQueryChanged)
          if (query.isEmpty()) {
            observeAllPosts()
          }
          return false
        }
      })
      setOnQueryTextFocusChangeListener { view, hasFocus ->
        if (hasFocus) {
          view.findFocus().focusAndShowKeyboard()
//          TextHelper.showKeyboard(view.findFocus())
          setOnQueryTextFocusChangeListener(null)
        }
      }
      requestFocus()
    }
    observePosts()
  }

  private fun observeAllPosts() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.allPostState.collect {
        Log.d("TAGA", "all observePosts: $it")
        if (it != null) {
          adapter.addAll(it)
        }
      }
    }
  }

  private fun observePosts() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.postState.collect {
        if (it?.isEmpty() == true) {
          binding.noSearchResultView.show()
          binding.rvSearchResult.hide()
        } else {
          it?.let {
            adapter.addAll(it)
          }
          binding.noSearchResultView.hide()
          binding.rvSearchResult.show()
        }
      }
    }
  }

  private fun observeFilters(filterInfo: FilterInfo?) {
    filterInfo?.let { viewModel.applyFilter(it) }
  }

  private fun observeFavState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.favState.collect {
        Log.d("TAGA", "hjghjgjhghjghj: $it")
        if (it == true) {
          filterData?.let {
            viewModel.applyFilter(it)
            viewModel.getOpportunities()
          } ?: viewModel.getOpportunities()
          viewModel.clearFavState()
        }
      }
    }
  }

  companion object {
    const val CATEGORY_ID = "categoryId"
    const val FILTER_KEY = "filter_key"
    const val FILTER_DATA_RESULT = "filter_data"
    const val STORED_FILTER_DATA = "stored_filter_data"
  }
}