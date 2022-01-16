package kz.kbtu.diplomaproject.presentation.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.isValidEmail
import kz.airba.infrastructure.helpers.makeLinks
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.MainActivity
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentSignUpBinding
import kz.kbtu.diplomaproject.presentation.auth.AuthState.EMPTY
import kz.kbtu.diplomaproject.presentation.auth.AuthState.INVALID
import kz.kbtu.diplomaproject.presentation.auth.AuthState.PASSWORD
import kz.kbtu.diplomaproject.presentation.auth.AuthState.USER_EXIST
import kz.kbtu.diplomaproject.presentation.auth.AuthState.USER_NOT_EXIST
import kz.kbtu.diplomaproject.presentation.auth.AuthState.VALID
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment() {
  override val viewModel: AuthViewModel by viewModel()
  private lateinit var binding: FragmentSignUpBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    observeSignUp()
    binding.tvHaveAccount.makeLinks(
      Pair(
        getString(R.string.login_link_sign_in),
        View.OnClickListener {
          navigateSafely(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment2())
        })
    )
  }

  private fun bindViews() {
    binding.btnSave.setOnClickListener {
      createUser()
    }
  }

  private fun createUser() {
    val username = binding.tilEmail.editText?.text.toString()
    val psw = binding.tilPassword.editText?.text.toString()
    val psw2 = binding.tilPassword2.editText?.text.toString()
    if (username.isEmpty()) {
      binding.tilEmail.error = "Please enter the email"
    }
    if (psw2.isEmpty()) {
      binding.tilPassword2.error = "Please enter the password"
    }
    if (psw.isEmpty()) {
      binding.tilPassword.error = "Please enter the password"
    }
    if (username.isNotEmpty() && psw.isNotEmpty() && psw2.isNotEmpty()) {
      if (username.isValidEmail()) {
        binding.loadingBtnProgress.visibility = View.VISIBLE
        viewModel.createUser(username, psw, psw2)
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
            viewModel.clearState()
          }
          USER_EXIST -> {
            Toasty.error(requireContext(), "User already registered !", Toast.LENGTH_SHORT, true)
              .show()
            viewModel.clearState()
          }
          USER_NOT_EXIST -> {
          }
          PASSWORD -> {
            Toasty.error(requireContext(), "Passwords must be same", Toast.LENGTH_SHORT, true)
              .show()
            viewModel.clearState()
          }
        }
      }
    }
  }

  private fun openMainContainer() {
    (activity as? MainActivity)?.openMainContainer()
  }

}