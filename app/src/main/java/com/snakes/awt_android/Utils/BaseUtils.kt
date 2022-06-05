package com.snakes.awt_android.Utils


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context


import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

import com.horizam.skbhub.Utils.Constants.Companion.urlWebView
import com.snakes.awt_android.Activities.WebActivity
import com.snakes.awt_android.App
import com.snakes.awt_android.R
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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

        @RequiresApi(Build.VERSION_CODES.N)
        fun showDatePicker(text: EditText, context: Context) {
            val datePickerDialog = DatePickerDialog(context, R.style.Dialog)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                var monthS = ""
                var dayS = ""
                monthS = if (month > 8) {
                    (month + 1).toString()
                } else {
                    "0" + (month + 1)
                }
                dayS = if (dayOfMonth > 9) {
                    dayOfMonth.toString()
                } else {
                    "0$dayOfMonth"
                }
                text.setText("$year-$monthS-$dayS")
            }
            datePickerDialog.show()
        }

        fun showTimePicker(text: EditText, context: Context) {
            val mCurrentTime = Calendar.getInstance()
            val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mCurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog = TimePickerDialog(context, R.style.Dialog,
                { view, hourOfDay, minute ->
                    val time = "$hourOfDay:$minute"
                    val fmt = SimpleDateFormat("HH:mm")
                    var date: Date? = null
                    try {
                        date = fmt.parse(time)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }
                    val fmtOut = SimpleDateFormat("hh:mm:ss")
                    val formattedTime = fmtOut.format(date)
                    text.setText(formattedTime)
                }, hour, minute, false
            )
            mTimePicker.show()
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

        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }

    }
}