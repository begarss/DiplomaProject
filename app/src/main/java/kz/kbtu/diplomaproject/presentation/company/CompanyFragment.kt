package kz.kbtu.diplomaproject.presentation.company

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
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kz.airba.infrastructure.helpers.focusAndShowKeyboard
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentCompanyBinding
import kz.kbtu.diplomaproject.domain.helpers.operators.debounce
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyFragment : BaseFragment() {
  override val viewModel: CompanyViewModel by viewModel()
  private lateinit var binding: FragmentCompanyBinding

  private val adapter by lazy {
    CompanyAdapter(type = CompanyHolder.REGULAR, onItemClick = {
      navigateSafely(CompanyFragmentDirections.actionCompanyFragmentToCompanyDetail(it))
    }, onFollowClick = {
      viewModel.makeSubscribe(it)
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_company, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getCompanies()
    bindViews()
    setUpSearchView()
    observeAllCompanies()
    observeFollowState()
  }

  private fun bindViews() {
    with(binding) {
      searchFragmentToolbar.inflateMenu(R.menu.search_menu)
      searchFragmentToolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

      rvSearchResult.adapter = adapter
    }
  }

  private fun setUpSearchView() {
    val onQueryChanged = lifecycleScope.debounce<String> {
      if (it.length >= 3) {
        viewModel.searchCompany(it)
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
      searchView.queryHint = "Search Company"
      v.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
      setOnQueryTextListener(object : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?) = false

        override fun onQueryTextChange(newText: String?): Boolean {
          newText?.let(onQueryChanged)
          if (query.isEmpty()) {
            observeAllCompanies()
          }
//          if (query.length < 3) {
//
//          }
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
    observeSearchCompanies()
//    handleLoadStates(true)
  }

  private fun observeAllCompanies() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.allCompanyState.collect {
        Log.d("TAGA", "observePosts: $it")
        if (it != null) {
          adapter.addAll(it)
        }
      }
    }
  }

  private fun observeSearchCompanies() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.companyState.collect {
        Log.d("TAGA", "observePosts: $it")
        if (it != null) {
          adapter.addAll(it)
        }
      }
    }
  }

  private fun observeFollowState() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.followState.collect {
        if (it == true) {
          viewModel.getCompanies()
          viewModel.clearFollowState()
        }
      }
    }
  }

}