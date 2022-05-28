package com.example.people4animals.domain.user.model

import kotlin.collections.ArrayList

data class Report(
    var id: String = "",
    var ownerId: String = "",
    var status: String = "",
    var date: Long = 0, // TODO: Check if its better to make it null
    var title: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var description: String = "",
    var photosIds: ArrayList<String> = ArrayList(),
    var distance: Double = 0.0,

)

