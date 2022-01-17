package kz.kbtu.diplomaproject.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.getText
import kz.airba.infrastructure.helpers.onBackPresses
import kz.kbtu.diplomaproject.MainActivity
import kz.kbtu.diplomaproject.databinding.FragmentOtpVerifyBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class OtpVerifyFragment : BaseFragment() {
  override val viewModel: AuthViewModel by viewModel()
  private lateinit var binding: FragmentOtpVerifyBinding

  private val email by lazy {
    requireArguments().getString(EMAIL_VERIFY)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentOtpVerifyBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    observeVerifyOtp()
  }

  private fun bindViews() {
    with(binding) {

      tvVerifyInfo.append(email)
      tilCode.editText?.doOnTextChanged { text, start, before, count ->
        btnVerify.isEnabled = !text.isNullOrEmpty() && text.isNotBlank()
        tilCode.error = null
      }

      btnVerify.setOnClickListener {
        val code = tilCode.getText()
        email?.let { it1 -> viewModel.verifyOtp(it1, code) }
      }
    }
  }

  private fun observeVerifyOtp() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.sendState.collect {
        if (it == true) {
          openMainContainer()//todo verify check
        } else if (it == false) {
          binding.tilCode.error = "Incorrect code"
        }
      }
    }
  }

  private fun openMainContainer() {
    (activity as? MainActivity)?.openMainContainer()
  }

  companion object {
    const val EMAIL_VERIFY = "email_verify"
  }
}