package com.snakes.awt_android.Models

import java.io.Serializable

data class SchoolKhana(
    var id:String? = null,
    var name:String? = null,
    var description: String? = null,
    var date: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var locatoion: String? = null,
    var lat: Double? = null,
    var long: Double? = null,
    var details: String? = null,
    var photo: String? = null
): Serializable