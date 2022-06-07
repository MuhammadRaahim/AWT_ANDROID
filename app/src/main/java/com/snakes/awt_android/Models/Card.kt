package com.snakes.awt_android.Models

import android.app.Application

data class Card (
    var cardNumber: String,
    var expireDate: String,
    var cardAid : ByteArray,
    var application: String,
    var cardType: String,
    var pinTryLeft: String,
    var cardIssuer: String,
    var save: Boolean
    )