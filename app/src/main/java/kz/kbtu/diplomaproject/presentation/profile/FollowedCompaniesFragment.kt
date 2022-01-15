package kz.kbtu.diplomaproject.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.navigateSafely
import kz.airba.infrastructure.helpers.show
import kz.kbtu.diplomaproject.databinding.FragmentSubscribedCompanyBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.base.MenuItemType.COMPANY
import kz.kbtu.diplomaproject.presentation.company.CompanyAdapter
import kz.kbtu.diplomaproject.presentation.company.CompanyFragmentDirections
import kz.kbtu.diplomaproject.presentation.company.CompanyHolder.FOLLOWED
import kz.kbtu.diplomaproject.presentation.company.CompanyHolder.REGULAR
import kz.kbtu.diplomaproject.presentation.company.CompanyViewModel
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FollowedCompaniesFragment : BaseFragment() {
  override val viewModel: CompanyViewModel by viewModel()
  private lateinit var binding: FragmentSubscribedCompanyBinding
  private val sharedViewModel: SharedViewModel by sharedStateViewModel()

  private val adapter by lazy {
    CompanyAdapter(type = FOLLOWED, onItemClick = { id, isFollowed ->
      navigateSafely(
        FollowedCompaniesFragmentDirections.actionFollowedCompaniesFragmentToCompanyDetail(
          id
        ).setIsFollowed(isFollowed)
      )
    }, onFollowClick = {
      viewModel.makeSubscribe(it)
    })
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentSubscribedCompanyBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getSubscribedCompany()
    observeCompanies()
    bindViews()
  }

  private fun bindViews() {
    with(binding) {
      toolbar.toolbar.apply {
        title = "Followed companies"
        setNavigationOnClickListener {
          requireActivity().onBackPressed()
        }
      }

      rvSubscribedCompanies.adapter = adapter
      emptyView.btnOpenCompany.setOnClickListener {
        sharedViewModel.selectMenuItem(COMPANY)
      }
    }
  }

  private fun observeCompanies() {
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.companyState.collect {
        if (it?.isEmpty() == true) {
          binding.emptyView.root.show()
          binding.rvSubscribedCompanies.hide()
        } else {
          it?.let {
            adapter.addAll(it)
          }
          binding.emptyView.root.hide()
          binding.rvSubscribedCompanies.show()
        }
      }
    }
  }

}