package kz.kbtu.diplomaproject.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kz.airba.infrastructure.helpers.makeLinks
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentSignUpBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment() {
  override val viewModel: LoginViewModel by viewModel()
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
    binding.tvHaveAccount.makeLinks(
      Pair(
        getString(R.string.login_link_sign_in),
        View.OnClickListener {
          navigateSafely(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment2())
        })
    )
  }
}