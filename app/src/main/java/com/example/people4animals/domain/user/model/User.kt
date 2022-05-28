package com.example.people4animals.domain.user.model

import java.util.*

data class User(
    val id: String = "",
    val username: String = "",
    val name: String = "",
    val phone: String = "",
    val city: String = "",
    var photoID:String? =  null
    )
