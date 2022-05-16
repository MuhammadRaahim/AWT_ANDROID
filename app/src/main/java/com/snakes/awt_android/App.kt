package com.snakes.awt_android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        context = applicationContext
    }

    companion object{

        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

        fun getAppContext(): Context? {
            return context
        }
    }
}