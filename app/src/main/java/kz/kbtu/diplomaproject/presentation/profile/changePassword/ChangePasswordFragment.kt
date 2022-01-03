package kz.kbtu.diplomaproject.presentation.profile.changePassword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.getText
import kz.airba.infrastructure.helpers.hide
import kz.airba.infrastructure.helpers.hideKeyboard
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentChangePasswordBinding
import kz.kbtu.diplomaproject.presentation.auth.AuthState
import kz.kbtu.diplomaproject.presentation.auth.AuthState.USER_EXIST
import kz.kbtu.diplomaproject.presentation.auth.AuthState.USER_NOT_EXIST
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.EMPTY
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.INVALID
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.VALID
import kz.kbtu.diplomaproject.presentation.auth.ChangeState.WRONG_PASSWORD
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : BaseFragment() {
  override val viewModel: ProfileViewModel by viewModel()
  private lateinit var binding: FragmentChangePasswordBinding

  private val email by lazy {
    requireArguments().getString(EMAIL)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    listenEditTextChange()
    observeChangeState()
    observeSignUp()
  }

  private fun bindViews() {
    with(binding) {
      toolbar.toolbar.title = "Change password"
      toolbar.toolbar.navigationIcon = null

      btnChange.setOnClickListener {
        val oldPsw = tilOldPassword.getText()
        val newPsw = tilNewPassword.getText()
        viewModel.changePassword(oldPsw, newPsw)
      }
    }
  }

  private fun listenEditTextChange() {
    with(binding) {
      tilOldPassword.editText?.doOnTextChanged { text, _, _, _ ->
        tilOldPassword.error = null
        val newText = tilNewPassword.getText()
        btnChange.isEnabled =
          !text.isNullOrEmpty() && !text.isNullOrBlank() && !newText.isEmpty()
      }
      tilNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
        val oldText = tilOldPassword.getText()
        tilNewPassword.error = null
        btnChange.isEnabled = !text.isNullOrEmpty() && !text.isNullOrBlank() && !oldText.isEmpty()
      }
    }
  }

  private fun observeChangeState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.changePswState.collect {
        when (it) {
          EMPTY -> {

          }
          VALID -> {
            email?.let { it1 -> viewModel.login(it1, binding.tilNewPassword.getText()) }
          }
          WRONG_PASSWORD -> {
            binding.tilOldPassword.error = "Please, check your current password"
            viewModel.clearState()
          }
          INVALID -> {
            viewModel.clearState()
          }
        }
      }
    }
  }

  private fun observeSignUp() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.authState.collect {
        when (it) {
          AuthState.EMPTY -> {
          }
          AuthState.VALID -> {
            Snackbar.make(
              binding.btnChange,
              getString(R.string.snack_sucess_edited),
              Snackbar.LENGTH_LONG
            ).show()
            with(binding) {
              tilNewPassword.editText?.text?.clear()
              tilOldPassword.editText?.text?.clear()
              hideKeyboard()
              this.root.clearFocus()
            }
            viewModel.clearState()
          }
          AuthState.INVALID -> {
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

  companion object {
    const val EMAIL = "email"
  }
}