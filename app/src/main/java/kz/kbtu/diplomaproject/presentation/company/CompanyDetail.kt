package kz.kbtu.diplomaproject.presentation.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.kbtu.diplomaproject.databinding.FragmentCompanyBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompanyDetail : BaseFragment() {
  override val viewModel: CompanyViewModel by viewModel()
  private lateinit var binding: FragmentCompanyBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentCompanyBinding.inflate(inflater, container, false)
    return binding.root
  }
}