package kz.kbtu.diplomaproject.presentation.support

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.airba.infrastructure.helpers.navigateSafely
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.databinding.FragmentSupportBinding

class SupportFragment : BottomSheetDialogFragment() {

  private lateinit var binding: FragmentSupportBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentSupportBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding) {
      mail.setOnClickListener {
        composeEmail(address = arrayOf("ithuntkz@gmail.com"))
      }
      whatsapp.setOnClickListener {
        openWhatsApp()
      }
      telegram.setOnClickListener {
        val telegram = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/begarss"))
        telegram.setPackage("org.telegram.messenger")
        startActivity(telegram)
      }
      btnSuggest.setOnClickListener {
        composeEmail(address = arrayOf("ithuntkz@gmail.com"))
      }
      btnOnlineChat.setOnClickListener {
        navigateSafely(
          R.id.action_supportFragment_to_webViewFragment,
          bundleOf(WebViewFragment.WEB_URL to "http://supportchaty.herokuapp.com/chat/a/")
        )
      }
    }
  }

  private fun openWhatsApp() {
    val contact = "+77058883206" // use country code with your phone number

    val url = "https://api.whatsapp.com/send?phone=$contact"
    try {
      val pm = context!!.packageManager
      pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
      val i = Intent(Intent.ACTION_VIEW)
      i.data = Uri.parse(url)
      startActivity(i)
    } catch (e: NameNotFoundException) {
      Toast.makeText(
        requireContext(),
        "Whatsapp app not installed in your phone",
        Toast.LENGTH_SHORT
      ).show()
      e.printStackTrace()
    }
  }

  private fun composeEmail(address: Array<String>) {
    try {
      val intent = Intent(Intent.ACTION_SENDTO)
      intent.data = Uri.parse("mailto:") // only email apps should handle this
      intent.putExtra(Intent.EXTRA_EMAIL, address)
      startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
      Toast.makeText(requireContext(), "No email client", Toast.LENGTH_SHORT).show()
    }

  }
}