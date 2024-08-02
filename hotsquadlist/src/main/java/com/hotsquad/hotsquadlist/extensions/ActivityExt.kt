package com.hotsquad.hotsquadlist.extensions

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotsquad.hotsquadlist.R
import com.hotsquad.hotsquadlist.listener.DialogListeners

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun String?.isValid() = this != null && this.isNotEmpty() && !this.equals("null", true)

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider.NewInstanceFactory().create(viewModelClass)

fun <T : AppCompatActivity> AppCompatActivity.gotoActivity(targetActivityClass: Class<T>) {
    val intent = Intent(this, targetActivityClass)
    startActivity(intent)
}

fun <T : AppCompatActivity> Fragment.gotoActivityFromFragment(targetActivityClass: Class<T>) {
    val intent = Intent(requireActivity(), targetActivityClass)
    startActivity(intent)
}

fun Fragment.gotoActivityFromFragment(
    targetActivityClass: Class<*>,
    intentKey: String,
    intentValue: Any? = null
) {
    val i = Intent(requireActivity(), targetActivityClass)
    i.putExtra(intentKey, intentValue)
    startActivity(i)
}

fun Fragment.gotoActivityFromFragment(
    targetActivityClass: Class<*>,
    intentKey: String,
    intentValue: Any? = null,
    intentKey1: String,
    intentValue1: Any? = null

) {
    val i = Intent(requireActivity(), targetActivityClass)
    i.putExtra(intentKey, intentValue)
    i.putExtra(intentKey1, intentValue1)
    startActivity(i)
}

fun Fragment.gotoActivityWithNoHistoryFromFragment(targetActivityClass: Class<*>) {
    val i = Intent(requireActivity(), targetActivityClass)
    i.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
}

fun AppCompatActivity.gotoActivityWithNoHistory(targetActivityClass: Class<*>) {
    val i = Intent(this, targetActivityClass)
    i.flags =
        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
}

fun AppCompatActivity.gotoActivity(
    targetActivityClass: Class<*>,
    intentKey: String,
    intentValue: Any? = null
) {
    val i = Intent(this, targetActivityClass)
    i.putExtra(intentKey, intentValue)
    startActivity(i)
}

fun AppCompatActivity.gotoActivity(
    targetActivityClass: Class<*>,
    intentKey1: String,
    intentValue1: Any? = null,
    intentKey2: String,
    intentValue2: Any? = null
) {
    val i = Intent(this, targetActivityClass)
    i.putExtra(intentKey1, intentValue1)
    i.putExtra(intentKey2, intentValue2)
    startActivity(i)
}

fun AppCompatActivity.gotoActivity(
    targetActivityClass: Class<*>,
    intentKey1: String,
    intentValue1: Any? = null,
    intentKey2: String,
    intentValue2: Any? = null,
    intentKey3: String,
    intentValue3: Any? = null
) {
    val i = Intent(this, targetActivityClass)
    i.putExtra(intentKey1, intentValue1)
    i.putExtra(intentKey2, intentValue2)
    i.putExtra(intentKey3, intentValue3)
    startActivity(i)
}

fun AppCompatActivity.gotoActivity(
    targetActivityClass: Class<*>,
    intentKey1: String,
    intentValue1: Any? = null,
    intentKey2: String,
    intentValue2: Any? = null,
    intentKey3: String,
    intentValue3: Any? = null,
    intentKey4: String,
    intentValue4: Any? = null
) {
    val i = Intent(this, targetActivityClass)
    i.putExtra(intentKey1, intentValue1)
    i.putExtra(intentKey2, intentValue2)
    i.putExtra(intentKey3, intentValue3)
    i.putExtra(intentKey4, intentValue4)
    startActivity(i)
}

fun AppCompatActivity.changeStatusBarColor(@ColorRes color : Int) {
    val window = this.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = ContextCompat.getColor(this,color)
}

fun AppCompatActivity.changeNavigationBarColor(@ColorRes color : Int) {
    val window = this.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.navigationBarColor = ContextCompat.getColor(this,color)
}

fun Fragment.changeStatusBarColor(@ColorRes color : Int) {
    val window = requireActivity().window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = ContextCompat.getColor(requireContext(),color)
}

fun AppCompatActivity.setTransparentStatusBar() {
    this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    //activity.window.navigationBarColor = R.color.black_light
    this.window.statusBarColor = Color.TRANSPARENT
}

fun AppCompatActivity.setStatusBarWithBlackIcon(@ColorRes color : Int) {
    this.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    this.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    //activity.window.navigationBarColor = R.color.black_light
    this.window.statusBarColor = ContextCompat.getColor(this, color)
}

fun AppCompatActivity.showMaterialAlertDialog(message: String, listener: DialogListeners? = null) {
    MaterialAlertDialogBuilder(this)
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
            listener?.onPositionButtonTap(dialog)
        }
        .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
            listener?.onNegativeButtonTap(dialog)
        }
        .show()
}

fun <T> List<T>.customFilterList(filterFunction: (T) -> (Boolean)): List<T> {
    val resultList = mutableListOf<T>()
    for (item in this) {
        if (filterFunction(item)) {
            resultList.add(item)
        }
    }
    return resultList
}