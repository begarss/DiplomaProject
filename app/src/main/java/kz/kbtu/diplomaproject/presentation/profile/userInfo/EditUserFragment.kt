package kz.kbtu.diplomaproject.presentation.profile.userInfo

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.airba.infrastructure.helpers.hide
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentProfieInfoBinding
import kz.kbtu.diplomaproject.presentation.base.BaseFragment
import kz.kbtu.diplomaproject.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditUserFragment : BaseFragment() {

  override val viewModel: ProfileViewModel by viewModel()
  private lateinit var binding: FragmentProfieInfoBinding
  private val args: EditUserFragmentArgs by navArgs()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profie_info, container, false)

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindViews()
    checkChanges()
    observeEditState()
  }

  private fun bindViews() {
    binding.toolbar.toolbar.apply {
      this.title = "Ваши данные"
      this.setNavigationOnClickListener {
        requireActivity().onBackPressed()
      }
    }

    Log.d("TAGQ", "onViewCreated: ${args.userInfo}")
    binding.tilUsername.editText?.setText(args.userInfo?.firstName)
    binding.tilPhone.editText?.setText(args.userInfo?.contactNumber)
    if (!args.userInfo?.birthday.isNullOrEmpty())
      binding.tilDate.editText?.setText(formatRestDate(args.userInfo?.birthday))

//    disableDateEditor()

    val datePicker =
      MaterialDatePicker.Builder.datePicker()
        .setTitleText(getString(R.string.date_pciker_title))
        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        .setTheme(R.style.ThemeOverlay_App_DatePicker)
        .build()

    binding.tilDate.editText?.setOnClickListener {
      datePicker.show(requireActivity().supportFragmentManager, "tag")

    }

    datePicker.addOnPositiveButtonClickListener {
      val outputFormat: DateFormat = SimpleDateFormat(BIRTH_PATTERN, Locale.getDefault())
      val date = Date(it)
      binding.tilDate.editText?.setText(outputFormat.format(date))
    }

    binding.btnSave.setOnClickListener {
      val name = binding.tilUsername.editText?.text.toString().checkText()
      val date = binding.tilDate.editText?.text.toString().checkText()
      val phone = binding.tilPhone.editText?.text.toString().checkText()
      viewModel.setUserInfo(userName = name, birthDay = date, phone = phone)
    }

    val letterFilter = InputFilter { source, start, end, dest, dstart, dend ->
      var filtered = ""
      for (i in start until end) {
        val character = source[i]
        if (!Character.isWhitespace(character) && Character.isLetter(character)) {
          filtered += character
        }
      }
      filtered
    }
    binding.tilUsername.editText?.filters = arrayOf(letterFilter)

  }

  private fun disableDateEditor() {
    if (!binding.tilDate.editText?.text.isNullOrEmpty()) {
      binding.tilDate.editText?.apply {
        isFocusable = false
        isClickable = false
        inputType = InputType.TYPE_NULL
        isEnabled = false
        setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
      }
    }
  }

  private fun checkChanges() {
    binding.tilUsername.editText?.addTextChangedListener {
      if (it.toString().length < 2)
        showButton(isEnabled = false)
      else
        showButton(isEnabled = true)
    }
    MaskedTextChangedListener.Companion.installOn(
      binding.etPhone, PHONE_MASK, object : MaskedTextChangedListener.ValueListener {
        override fun onTextChanged(
          maskFilled: Boolean,
          extractedValue: String,
          formattedValue: String
        ) {
          if (!maskFilled)
            showButton(isEnabled = false)
          else
            showButton(isEnabled = true)
        }
      })

    binding.tilDate.editText?.addTextChangedListener {
      showButton()
    }
  }

  private fun showButton(isEnabled: Boolean? = true) {
    binding.btnSave.isGone = false
    if (isEnabled != null) {
      binding.btnSave.isEnabled = isEnabled
    }
  }

  private fun String?.checkText(): String? {
    return if (this.isNullOrEmpty())
      null
    else
      this
  }

  private fun observeEditState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewModel.editState.collectLatest {
        if (it) {
          binding.tilUsername.clearFocus()
          Snackbar.make(
            binding.btnSave,
            getString(R.string.snack_sucess_edited),
            Snackbar.LENGTH_LONG
          )
            .show()
//          disableDateEditor()
          viewModel.clearState()
          binding.btnSave.hide()
        }
      }
    }
  }

  private fun formatRestDate(birthDate: String?): String {
    val inputFormat: DateFormat = SimpleDateFormat(REQUEST_BIRTH_PATTERN, Locale.getDefault())
    val outputFormat: DateFormat =
      SimpleDateFormat(BIRTH_PATTERN, Locale.getDefault())
    return outputFormat.format(inputFormat.parse(birthDate))

  }

  companion object {
    const val BIRTH_PATTERN = "d MMMM yyyy"
    const val REQUEST_BIRTH_PATTERN = "yyyy-MM-dd"
    const val PHONE_MASK = "+7 ([000]) [000]-[00]-[00]"
    const val PHONE_PREFIX = "+7"

  }

}