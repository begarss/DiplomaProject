package kz.kbtu.diplomaproject.presentation.loading

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import kz.airba.infrastructure.helpers.dpToPixelInt
import kz.kbtu.diplomaproject.R

class LoadingDialog(context: Context?) {

  private val dialog: Dialog
  private val loading: ProgressBar
  private val mainView: View

  init {
    val alertDialog = AlertDialog.Builder(context)
    mainView = LayoutInflater.from(context).inflate(R.layout.fragment_loading, null)
    loading = mainView.findViewById(R.id.loading)
    dialog = alertDialog.setView(mainView)
      .create()
    dialog?.window?.setDimAmount(0.0f)
  }

  fun showLoading() {
    dialog.apply {
      show()
      window?.setLayout(mainView.dpToPixelInt(100f), mainView.dpToPixelInt(100f))
    }
  }

  fun hideLoading() {
    dialog.dismiss()
  }
}
