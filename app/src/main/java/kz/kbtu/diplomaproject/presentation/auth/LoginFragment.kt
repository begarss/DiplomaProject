package kz.kbtu.diplomaproject.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentLoginBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {
  override val viewModel: LoginViewModel by viewModel()
  private lateinit var binding: FragmentLoginBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
    return binding.root
  }
}