package com.horizam.skbhub.Utils

import android.content.Context
import android.content.SharedPreferences

class PrefManager( var context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(Constants.PREF_NAME,
        Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()


    var session:String
        get() = pref.getString(Constants.SESSION, "")!!
        set(value) {
            editor.putString(Constants.SESSION,value)
            editor.apply()
            editor.commit()
        }
}