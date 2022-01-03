package kz.airba.infrastructure.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kz.kbtu.diplomaproject.R
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import java.util.Locale

fun Fragment.navigateSafely(resId: Int, bundle: Bundle? = null) {
  view?.post {
    findNavController().navigate(resId, bundle)
  }
}

fun Fragment.navigateSafely(direction: NavDirections) {
  view?.post {
    findNavController().navigate(direction)
  }
}

fun getDefaultPhoneSuffix(): String {
  return if (Locale.getDefault().country.startsWith("RU", true)) {
    "+7"
  } else {
    "+"
  }
}

fun getFinishTimerTime(millisUntilFinished: Long) =
  String.format("%02d:%02d", (millisUntilFinished / 1000) / 60, (millisUntilFinished / 1000) % 60)

fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
  val spannableString = SpannableString(this.text)
  var startIndexOfLink = -1
  for (link in links) {
    val clickableSpan = object : ClickableSpan() {
      override fun updateDrawState(textPaint: TextPaint) {
        // use this to change the link color
        textPaint.color = ContextCompat.getColor(this@makeLinks.context, R.color.primary)
        // toggle below value to enable/disable
        // the underline shown below the clickable text
        textPaint.isUnderlineText = false
      }

      override fun onClick(view: View) {
        Selection.setSelection((view as TextView).text as Spannable, 0)
        view.invalidate()
        link.second.onClick(view)
      }
    }
    startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
//      if(startIndexOfLink == -1) continue // todo if you want to verify your texts contains links text
    spannableString.setSpan(
      clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
      Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
  }
  this.movementMethod =
    LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
  this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun Fragment.showDenyPermissionDialog(messageRes: Int) {
  MaterialAlertDialogBuilder(requireContext())
    .setMessage(messageRes)
    .setPositiveButton("OK") { dialog, which ->
      openAppSettings()
    }
    .setNegativeButton("Cancel") { dialog, which ->
      dialog.cancel()
    }
    .show()
}

private fun Fragment.openAppSettings() {
  val intent = Intent()
  intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
  val uri = Uri.fromParts("package", requireContext().packageName, null)
  intent.data = uri
  requireActivity().startActivity(intent)
}

fun RecyclerView.initRecyclerView(
  withDivider: Boolean = false,
  vertical: Boolean = true,
  withFixedSize: Boolean = false
) {
  val orientation = if (vertical) {
    LinearLayoutManager.VERTICAL
  } else {
    LinearLayoutManager.HORIZONTAL
  }
  val linearLayoutManager = LinearLayoutManager(this.context, orientation, false)
  layoutManager = linearLayoutManager

  if (withFixedSize) {
    setHasFixedSize(true)
  }
  if (withDivider) {
    val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//    ContextCompat.getDrawable(context, R.drawable.item_decoration_layer)?.let {
//      itemDecoration.setDrawable(
//        it
//      )
//      this.addItemDecoration(itemDecoration)
//    }

  }
}

fun TextInputLayout.getText(): String {
  return this.editText?.text.toString()
}

fun FragmentActivity.onBackPresses(
  viewLifecycleOwner: LifecycleOwner,
  function: () -> Unit
) {
  this.onBackPressedDispatcher.addCallback(viewLifecycleOwner,
    object : OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        function.invoke()
      }

    })
}

fun String.isValidEmail() =
  !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun View.setOnClickListenerWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
  this.setOnClickListener(object : View.OnClickListener {
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
      if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
      else action()

      lastClickTime = SystemClock.elapsedRealtime()
    }
  })
}

fun setImageUrl(it: OpportunityDTO) {
  it.company?.picture = "http://ithuntt.pythonanywhere.com/${it.company?.picture}"
}