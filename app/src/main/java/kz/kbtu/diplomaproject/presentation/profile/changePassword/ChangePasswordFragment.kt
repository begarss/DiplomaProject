package kz.kbtu.diplomaproject.presentation.profile.changePassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import kz.airba.infrastructure.helpers.getText
import kz.kbtu.diplomaproject.databinding.FragmentChangePasswordBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.base.BaseViewModel
import kz.kbtu.diplomaproject.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : BaseFragment() {
  override val viewModel: ProfileViewModel by viewModel()
  private lateinit var binding: FragmentChangePasswordBinding

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
  }

  private fun bindViews() {
    with(binding) {
      toolbar.toolbar.title = "Change password"
      toolbar.toolbar.navigationIcon = null

      btnChange.setOnClickListener {
        val oldPsw = tilOldPassword.getText()
        val newPsw = tilNewPassword.getText()

      }
    }
  }

  private fun listenEditTextChange() {
    with(binding) {
      tilOldPassword.editText?.doOnTextChanged { text, _, _, _ ->
        if (text.isNullOrEmpty() && text.isNullOrBlank()) {
          btnChange.isEnabled = false
        }
      }
      tilNewPassword.editText?.doOnTextChanged { text, _, _, _ ->
        if (text.isNullOrEmpty() && text.isNullOrBlank()) {
          btnChange.isEnabled = false
        }
      }
    }
  }
}