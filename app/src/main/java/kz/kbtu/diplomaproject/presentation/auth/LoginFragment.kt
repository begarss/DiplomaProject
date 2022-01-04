package kz.kbtu.diplomaproject.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.isValidEmail
import kz.airba.infrastructure.helpers.makeLinks
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.MainActivity
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentLoginBinding
import kz.kbtu.diplomaproject.presentation.auth.AuthState.EMPTY
import kz.kbtu.diplomaproject.presentation.auth.AuthState.INVALID
import kz.kbtu.diplomaproject.presentation.auth.AuthState.USER_EXIST
import kz.kbtu.diplomaproject.presentation.auth.AuthState.USER_NOT_EXIST
import kz.kbtu.diplomaproject.presentation.auth.AuthState.VALID
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {
  override val viewModel: AuthViewModel by viewModel()
  private lateinit var binding: FragmentLoginBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    observeSignUp()

  }

  private fun bindViews() {
    binding.btnSave.setOnClickListener {
      createUser()
    }
    binding.tvNoAccount.makeLinks(
      Pair(
        getString(R.string.login_link_sign_up),
        View.OnClickListener {
          navigateSafely(LoginFragmentDirections.actionLoginFragment2ToRegistrationFragment())
        })
    )
    binding.tvForgotPsw.makeLinks(Pair(getString(R.string.forgot_password), View.OnClickListener {
      navigateSafely(LoginFragmentDirections.actionLoginFragment2ToEmailVerifyFragment())
    }))
  }

  private fun createUser() {
    val username = binding.tilEmail.editText?.text.toString()
    val psw = binding.tilPassword.editText?.text.toString()
    if (username.isEmpty()) {
      binding.tilEmail.error = "Please enter the email"
    }
    if (psw.isEmpty()) {
      binding.tilPassword.error = "Please enter the password"
    }
    if (username.isNotEmpty() && psw.isNotEmpty()) {
      if (username.isValidEmail()) {
        binding.loadingBtnProgress.visibility = View.VISIBLE
        viewModel.login(username, psw)
      } else {
        binding.tilEmail.error = "Please enter correct email"
      }
    }
  }

  private fun observeSignUp() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.authState.collect {
        binding.loadingBtnProgress.hide()
        when (it) {
          EMPTY -> {
          }
          VALID -> {
            viewModel.clearState()
            openMainContainer()
          }
          INVALID -> {
            Toast.makeText(
              requireContext(),
              "Please check your email or password",
              Toast.LENGTH_SHORT
            ).show()
            viewModel.clearState()
          }
          USER_EXIST -> {
          }
          USER_NOT_EXIST -> {
            Toast.makeText(requireContext(), "User not registered yet!", Toast.LENGTH_SHORT).show()
          }
        }
      }
    }
  }

  private fun openMainContainer() {
    (activity as? MainActivity)?.openMainContainer()
  }
}