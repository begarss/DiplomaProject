package kz.kbtu.diplomaproject.presentation.explore.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.databinding.FragmentFilterBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.ExploreFragment
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ChipIds
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.CompanyModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ContractModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterGroupType
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterGroupType.CATEGORY
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterGroupType.CONTRACT
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.FilterInfo
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.JobTypeModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FilterFragment : BaseFragment() {
  override val viewModel: FilterViewModel by viewModel()
  private lateinit var binding: FragmentFilterBinding

  private var categoryList: List<JobCategory>? = null
  private var companyList: List<CompanyModel>? = null

  private val storedFilterChips by lazy {
    requireArguments().getParcelableArrayList<ChipIds>(STORED_CHIPS)
  }

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
    setChipsSelected()
  }

  private fun bindViews() {
    with(binding) {
      btnApply.setOnClickListener {
        val typeId = filterGroupJobType.checkedChipId
        val categoryId = filterGroupJobCategory.checkedChipId
        val contractId = filterGroupJobContract.checkedChipId
        val companyId = filterGroupJobCompany.checkedChipId
        var jobCategoryChip: Chip? = null
        var contractChip: Chip? = null
        if (typeId > -1) {
          jobCategoryChip =
            (filterGroupJobType.getChildAt(typeId) as Chip)
        }
        if (contractId > -1) {
          contractChip = (filterGroupJobContract.getChildAt(contractId) as Chip)
        }

        val categoryChecked = ChipIds(
          groupType = FilterGroupType.CATEGORY,
          groupId = filterGroupJobCategory.id,
          categoryId
        )
        val typeChecked =
          ChipIds(groupType = FilterGroupType.TYPE, groupId = filterGroupJobType.id, typeId)
        val contractChecked = ChipIds(
          groupType = FilterGroupType.CONTRACT,
          groupId = filterGroupJobContract.id,
          contractId
        )
        val companyChecked =
          ChipIds(groupType = FilterGroupType.COMPANY, filterGroupJobCompany.id, companyId)

        val filterInfo = FilterInfo(
          title = null,
          jobCategory = categoryId.takeIf {
            it > -1
          },
          jobType = jobCategoryChip?.text?.toString(),
          contractType = contractChip?.text?.toString(),
          companyId.takeIf {
            it > -1
          },
          savedChips = arrayListOf(categoryChecked, typeChecked, contractChecked, companyChecked)
        )

        setFragmentResult(
          requestKey = FILTER_KEY,
          result = bundleOf(
            FILTER_DATA_RESULT to
                filterInfo
          )
        )
        requireActivity().onBackPressed()
      }
      Log.d("TAGA", "bindViews: $storedFilterChips")
      storedFilterChips?.let {
        setChipsSelected()
      }
    }
  }

  private fun observeCategories() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.categoryState.collect {
        if (it != null) {
          categoryList = it
          setCategoryItems(it)
          setChipsSelected()
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
          setChipsSelected()
        }
      }
    }
  }

  private fun setCategoryItems(filterItems: List<JobCategory>) {
    binding.filterGroupJobCategory.id = CAT
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
    binding.filterGroupJobCompany.id = COMPANY
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
    setChipsSelected()
  }

  private fun observeTypes() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.typeState.collect {
        if (it != null) {
          setJobTypeItems(it)
          setChipsSelected()
        }
      }
    }
  }

  private fun setJobTypeItems(filterItems: List<JobTypeModel>) {
    binding.filterGroupJobType.id = TYPE
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
    binding.filterGroupJobContract.id = CONTRACT
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

  private fun setChipsSelected() {
    binding.apply {
      storedFilterChips?.forEach {
        with(binding) {
          when (it.groupId) {
            filterGroupJobCategory.id -> {
              filterGroupJobCategory.check(it.checkedId)
            }
            filterGroupJobType.id -> {
              filterGroupJobType.check(it.checkedId)
            }
            filterGroupJobContract.id -> {
              filterGroupJobContract.check(it.checkedId)
            }
            filterGroupJobCompany.id -> {
              filterGroupJobCompany.check(it.checkedId)
            }
          }
        }
      }
    }
  }

  companion object {
    const val FILTER_KEY = "filter_key"
    const val FILTER_DATA_RESULT = "filter_data"
    const val STORED_CHIPS = "stored_chips"
    const val CAT = 1
    const val TYPE = 2
    const val CONTRACT = 3
    const val COMPANY = 4
  }
}