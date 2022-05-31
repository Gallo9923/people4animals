package com.example.people4animals.domain.user.model

data class ReportUpdate(
    var authorID: String = "",
    var content: String = "",
    var date: Long = 0,
    var id: String = "",
    var photoID: String = ""
)

