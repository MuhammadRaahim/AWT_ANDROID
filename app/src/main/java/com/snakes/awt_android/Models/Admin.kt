package com.snakes.awt_android.Models

import java.io.Serializable

data class Admin(
     var id: String? = null,
     var userName: String? = null,
     var cnic: String? = null,
     var address: String? = null,
     var phone: String? = null,
     var email: String? = null,
     var referralCode: String? = null,
     var profileImage: String? = null

 ): Serializable
