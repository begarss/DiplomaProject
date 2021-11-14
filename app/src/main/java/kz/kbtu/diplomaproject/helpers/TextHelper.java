package kz.kbtu.diplomaproject.helpers;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.regex.Pattern;

public class TextHelper {

  public static void showKeyboard(final View view) {
    if (view == null) {
      return;
    }
    try {
      final InputMethodManager inputMethodManager =
          (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    } catch (Throwable e) {

    }
  }

  public static void hideKeyboard(final View view) {
    if (view == null) {
      return;
    }
    try {
      final InputMethodManager imm =
          (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } catch (Throwable e) {

    }
  }

  public static void showFocusAndKeyboardDialog(Dialog dialog, View view) {
    if (view != null) {
      view.requestFocus();
      dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
  }

  public static void showFocusAndKeyboard(final View view) {
    if (view != null) {
      view.requestFocus();
      showKeyboard(view);
    }
  }

  /**
   * IMPORTANT: Set parent container:
   * android:focusable="true"
   * android:focusableInTouchMode="true"
   */
  public static void hideFocusAndKeyboard(final View view) {
    if (view != null) {
      view.clearFocus();
      hideKeyboard(view);
    }
  }

  public static boolean isValidEmail(CharSequence target) {
    return target != null && !target.toString().isEmpty() &&
        Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
            .matcher(target).matches();
  }

  public static boolean isNotOnlySpace(CharSequence target) {
    return !TextUtils.isEmpty(target) && !target.toString().trim().matches("");
  }

  public static boolean isNumeric(String str) {
    NumberFormat formatter = NumberFormat.getInstance();
    ParsePosition pos = new ParsePosition(0);
    formatter.parse(str, pos);
    return str.length() == pos.getIndex();
  }
}
