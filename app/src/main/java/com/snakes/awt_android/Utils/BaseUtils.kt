package com.snakes.awt_android.Utils


import android.content.Context


import android.content.Intent

import android.net.Uri

import com.horizam.skbhub.Utils.Constants.Companion.urlWebView
import com.snakes.awt_android.Activities.WebActivity






class BaseUtils {

    companion object{

        private const val phoneAwt = "0300-3383383"

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

    }
}