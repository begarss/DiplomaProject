package kz.kbtu.diplomaproject.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.getText
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentEmailVerifyBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EmailVerifyFragment : BaseFragment() {
  override val viewModel: AuthViewModel by viewModel()
  private lateinit var binding: FragmentEmailVerifyBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentEmailVerifyBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    observeSendOtp()
  }

  private fun bindViews() {
    with(binding) {

      tilEmail.editText?.doOnTextChanged { text, start, before, count ->
        btnSendCode.isEnabled = !text.isNullOrEmpty() && text.isNotBlank()
      }

      btnSendCode.setOnClickListener {
        val email = tilEmail.getText()
        viewModel.sendOtp(email)
      }
    }
  }

  private fun observeSendOtp() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.sendState.collect {
        if (it == true) {
          navigateSafely(
            R.id.action_emailVerifyFragment_to_otpVerifyFragment,
            bundleOf(OtpVerifyFragment.EMAIL_VERIFY to binding.tilEmail.getText())
          )
        }
      }
    }
  }
}