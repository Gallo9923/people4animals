package com.example.people4animals.domain.user.manager

import android.util.Log
import com.example.people4animals.domain.user.model.User

object UserManager {
    fun signUp(user: User, password: String): User {
        //TODO: Create user in firestore and auth
        return user
    }

    fun findUserByUsername(): User {
        //TODO: Get this info from firestore
        return User(
            "pepit@gmail.com",
            "Pepito Perez",
            "3218291992",
            "Cali"
        )
    }
}