package com.snakes.awt_android.Utils


import android.app.Activity
import android.content.Context


import android.content.Intent

import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

import com.horizam.skbhub.Utils.Constants.Companion.urlWebView
import com.snakes.awt_android.Activities.WebActivity
import com.snakes.awt_android.App
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import java.io.File


class BaseUtils {

    companion object{

        private const val phoneAwt = "0300-3383383"

        fun showMessage(view: View, message: String) {
            Snackbar.make(
                view,
                message, Snackbar.LENGTH_LONG
            ).show()
        }

        fun phoneIntent(context: Context){
            val uri = "tel:" + phoneAwt.trim()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse(uri)
            context.startActivity(intent)
        }

        fun loadWebView(context: Context, url:String){
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(urlWebView, url)
            context.startActivity(intent)
        }

        fun Fragment.hideKeyboard() {
            view?.let { activity?.hideKeyboard(it) }
        }
        suspend fun compressFile(filePath: String): String {
            var file: File = Compressor.compress(
                App.getAppContext()!!,
                File(filePath), Dispatchers.Main)
            return file.path
        }

        fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }

        private fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }
}