package kz.kbtu.diplomaproject.presentation.explore.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.databinding.FragmentFilterBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.CompanyModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ContractModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.JobTypeModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment : BaseFragment() {
  override val viewModel: FilterViewModel by viewModel()
  private lateinit var binding: FragmentFilterBinding

  private var categoryList: List<JobCategory>? = null
  private var companyList: List<CompanyModel>? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentFilterBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel.getAllCategories()
    viewModel.getCompanies()
    viewModel.getContractTypes()
    viewModel.getJobTypes()
    bindViews()
    observeCategories()
    observeCompanies()
    observeContracts()
    observeTypes()
  }

  private fun bindViews() {
    with(binding) {
      filterGroupJobCategory.setOnCheckedChangeListener { group, checkedId ->
      }
    }
  }

  private fun observeCategories() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.categoryState.collect {
        if (it != null) {
          categoryList = it
          setCategoryItems(it)
        }
      }
    }
  }

  private fun observeCompanies() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.companyState.collect {
        if (it != null) {
          companyList = it
          setCompanyItems(it)
        }
      }
    }
  }

  private fun setCategoryItems(filterItems: List<JobCategory>) {
    filterItems.forEach { category ->
      val chipFilter =
        LayoutInflater.from(context)
          .inflate(R.layout.item_chip, binding.filterGroupJobCategory, false) as Chip
      chipFilter.text = category.title
      category.id.let {
        if (it != null) {
          chipFilter.id = it
        }
      }
      binding.filterGroupJobCategory.addView(chipFilter)
    }
  }

  private fun setCompanyItems(filterItems: List<CompanyModel>) {
    filterItems.forEach { companyModel ->
      val chipFilter =
        LayoutInflater.from(context)
          .inflate(R.layout.item_chip, binding.filterGroupJobCompany, false) as Chip
      chipFilter.text = companyModel.name
      companyModel.id.let {
        if (it != null) {
          chipFilter.id = it
        }
      }
      binding.filterGroupJobCompany.addView(chipFilter)
    }
  }

  private fun observeTypes() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.typeState.collect {
        if (it != null) {
          setJobTypeItems(it)
        }
      }
    }
  }

  private fun setJobTypeItems(filterItems: List<JobTypeModel>) {
    filterItems.forEach { model ->
      val chipFilter =
        LayoutInflater.from(context)
          .inflate(R.layout.item_chip, binding.filterGroupJobType, false) as Chip
      chipFilter.text = model.name
      model.id.let {
        chipFilter.id = it
      }
      binding.filterGroupJobType.addView(chipFilter)
    }
  }

  private fun observeContracts() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.contractState.collect {
        if (it != null) {
          setJobContractsItems(it)
        }
      }
    }
  }

  private fun setJobContractsItems(filterItems: List<ContractModel>) {
    filterItems.forEach { model ->
      val chipFilter =
        LayoutInflater.from(context)
          .inflate(R.layout.item_chip, binding.filterGroupJobContract, false) as Chip
      chipFilter.text = model.name
      model.id.let {
        chipFilter.id = it
      }
      binding.filterGroupJobContract.addView(chipFilter)
    }
  }
}