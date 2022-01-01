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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kz.airba.infrastructure.helpers.focusAndShowKeyboard
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentExploreBinding
import kz.kbtu.diplomaproject.domain.helpers.operators.debounce
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.filter.FilterFragment
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterInfo
import kz.kbtu.diplomaproject.presentation.home.PostAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreFragment : BaseFragment() {
  override val viewModel: SearchViewModel by viewModel()
  private lateinit var binding: FragmentExploreBinding
  private var filterData: FilterInfo? = null

  private val adapter by lazy {
    PostAdapter(arrayListOf(), onFavClick = {

    }, onItemClick = {

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
  }

  private fun bindViews() {
    with(binding) {
      searchFragmentToolbar.inflateMenu(R.menu.search_menu)
      searchFragmentToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

      rvSearchResult.initRecyclerView()
      rvSearchResult.adapter = adapter

      fabFilter.setOnClickListener {
        navigateSafely(ExploreFragmentDirections.actionExploreFragmentToFilterFragment())
      }
    }
  }

  private fun setUpSearchView() {
    val onQueryChanged = lifecycleScope.debounce<String> {
      if (it.length >= 3) {
        filterData?.let { filterInfo ->
          filterInfo.title = it
          viewModel.applyFilter(filterInfo)
        } ?: viewModel.applyFilter(
          FilterInfo(
            title = it,
            null,
            null,
            null,
            null
          )
        )
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
        Log.d("TAGA", "observePosts: $it")
        if (it != null) {
          adapter.addAll(it)
        }
      }
    }
  }

  private fun observeFilters(filterInfo: FilterInfo?) {
    filterInfo?.let { viewModel.applyFilter(it) }
  }
}