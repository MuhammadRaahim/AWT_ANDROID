package com.snakes.awt_android.CallBacks

interface GenericHandler {
    fun showProgressBar(show:Boolean = false)
    fun showMessage(message:String)
    fun showNoInternet(show:Boolean = false)
}