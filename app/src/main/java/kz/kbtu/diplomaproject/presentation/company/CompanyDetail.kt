package kz.kbtu.diplomaproject.presentation.company

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.initRecyclerView
import kz.airba.infrastructure.helpers.load
import kz.kbtu.diplomaproject.databinding.FragmentCompanyBinding
import kz.kbtu.diplomaproject.databinding.FragmentCompanyDetailBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.home.PostAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyDetail : BaseFragment() {
  override val viewModel: CompanyViewModel by viewModel()
  private lateinit var binding: FragmentCompanyDetailBinding
  private val args: CompanyDetailArgs by navArgs()

  private val postAdapter by lazy {
    PostAdapter(arrayListOf(), onItemClick = {}, onFavClick = {})
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentCompanyDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getCompanyDetail(args.companyId)
    viewModel.getOppByCategory(args.companyId)
    bindViews()
    observeCompany()
    observeOpps()
  }

  private fun bindViews() {
    with(binding) {
      toolbar.toolbar.title = "Company"
      toolbar.toolbar.setNavigationOnClickListener {
        requireActivity().onBackPressed()
      }

      rvOpports.initRecyclerView()
      rvOpports.adapter = postAdapter
    }
  }

  private fun observeCompany() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.companyDetailState.collect {
        with(binding) {
          ivCompany.load(it?.picture)
          tvCompanyName.text = it?.name
          tvAboutCompany.text = it?.aboutCompany
        }
      }
    }
  }

  private fun observeOpps() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.companyOppState.collect {
        it?.let {
          Log.d("TAGA", "observeOpps: $it")
          postAdapter.addAll(it)
        }
      }
    }
  }

}