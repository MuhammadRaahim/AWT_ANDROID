package com.snakes.awt_android.Models

import java.io.Serializable

data class Service(
    var id: String? = null,
    var serviceName: String? = null,
    var description: String? = null,
    var details: String? = null,
    var donation: Int? = null,
    var serviceImage: String? = null
): Serializable