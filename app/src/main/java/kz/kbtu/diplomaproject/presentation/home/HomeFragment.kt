package kz.kbtu.diplomaproject.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import kz.kbtu.diplomaproject.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {
  override val viewModel: SharedViewModel by viewModel()
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_login, container, false)

  }
}