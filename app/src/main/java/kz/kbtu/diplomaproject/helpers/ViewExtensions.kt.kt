package kz.airba.infrastructure.helpers

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.Resources
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

fun View.onBackKey(block: () -> Unit) {
  isFocusableInTouchMode = true
  requestFocus()
  setOnKeyListener(object : View.OnKeyListener {
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
      return if (keyCode == KeyEvent.KEYCODE_BACK) {
        block()
        setOnKeyListener(null)
        true
      } else {
        false
      }
    }
  })

}

fun ViewPropertyAnimator.scale(scale: Float): ViewPropertyAnimator {
  return scaleY(scale)
    .scaleX(scale)
}

fun View.scale(scale: Float) {
  scaleX = scale
  scaleY = scale
}

var View.scale
  get() = (scaleY + scaleX) / 2
  set(value) {
    scale(value)
  }

fun View.setInvisible() {
  visibility = View.INVISIBLE
}

fun View.setGone() {
  visibility = View.GONE
}

fun View.setVisible() {
  visibility = View.VISIBLE
}

val View.transitionPair get() = this to transitionName
inline val Int.dp: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun View.enableChangingTransition() {
  (this as? ViewGroup)?.layoutTransition?.enableTransitionType(LayoutTransition.CHANGING)
}

fun View.dpToPixel(dp: Float): Float =
  dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun View.dpToPixel(dp: Int): Int =
  dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT).toInt()

fun View.dpToPixelInt(dp: Float): Int =
  (dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()

fun View.pixelToDp(px: Float): Float =
  px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)

fun View.spToPx(sp: Float): Int {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp,
    context.getResources().getDisplayMetrics()
  ).toInt()
}

fun View.spToPx(sp: Int): Float {
  return TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp.toFloat(),
    context.getResources().getDisplayMetrics()
  )
}

fun View.focusAndShowKeyboard() {
  /**
   * This is to be called when the window already has focus.
   */
  fun View.showTheKeyboardNow() {
    if (isFocused) {
      post {
        // We still post the call, just in case we are being notified of the windows focus
        // but InputMethodManager didn't get properly setup yet.
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
      }
    }
  }

  requestFocus()
  if (hasWindowFocus()) {
    // No need to wait for the window to get focus.
    showTheKeyboardNow()
  } else {
    // We need to wait until the window gets focus.
    viewTreeObserver.addOnWindowFocusChangeListener(
      object : ViewTreeObserver.OnWindowFocusChangeListener {
        override fun onWindowFocusChanged(hasFocus: Boolean) {
          // This notification will arrive just before the InputMethodManager gets set up.
          if (hasFocus) {
            this@focusAndShowKeyboard.showTheKeyboardNow()
            // It’s very important to remove this listener once we are done.
            viewTreeObserver.removeOnWindowFocusChangeListener(this)
          }
        }
      })
  }
}

fun ImageView.load(url: String?, placeholder: Int? = null) {
  Glide.with(this)
    .load(url)
    .let { builder ->
      placeholder?.let {
        builder.placeholder(it)
          .error(it)
      }
      builder
    }
    .into(this)
}

fun View.delayOnLifecycle(
  durationInMillis: Long,
  dispatcher: CoroutineDispatcher = Dispatchers.Main,
  block: () -> Unit
): Job? = findViewTreeLifecycleOwner()?.let { lifecycleOwner ->
  lifecycleOwner.lifecycle.coroutineScope.launch(dispatcher) {
    delay(durationInMillis)
    block()
  }

}

fun View.show() = switchVisibility(true)

/**
 * Убрать виджет из разметки (переключить видимость виджета в состояние [View.GONE])
 */
fun View.hide() = switchVisibility(false)

/**
 * Переключает видимость виджета между состояниями [View.VISIBLE] и [View.GONE], в зависимости от
 * переданного флага
 *
 * @param isShouldBeVisible  true - если нужно показать виджет, false - иначе
 */
fun View.switchVisibility(isShouldBeVisible: Boolean) {
  if ((this.visibility == View.VISIBLE && isShouldBeVisible) ||
    (this.visibility == View.GONE && !isShouldBeVisible)
  ) return
  visibility = if (isShouldBeVisible) {
    View.VISIBLE
  } else {
    View.GONE
  }
}

/**
 * Переключает видимость виджета между состояниями [View.VISIBLE] и [View.GONE]
 */
fun View.switchVisibility() {
  visibility =
    when (this.visibility) {
      View.VISIBLE -> View.GONE
      else -> View.VISIBLE
    }
}

