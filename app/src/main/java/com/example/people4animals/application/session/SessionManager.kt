package com.example.people4animals.application.session

import android.content.Context
import com.example.people4animals.configuration.SharedPreferencesManager
import com.example.people4animals.configuration.SingletonHolder
import com.example.people4animals.domain.user.manager.UserManager
import com.example.people4animals.domain.user.model.User
import com.google.gson.Gson

class SessionManager private constructor(context: Context) {

    private val sharedPreference: SharedPreferencesManager by lazy {
        SharedPreferencesManager(context)
    }

    fun getCurrentUser(): User? {
        val userRAW = sharedPreference.currentUser

        if (userRAW != "non-user") {
            return Gson().fromJson(sharedPreference.currentUser, User::class.java)
        }
        return null
    }

    fun setCurrentUser(user: User) {
        sharedPreference.currentUser = Gson().toJson(user)
    }

    fun logOut() {
        sharedPreference.currentUser = "non-user"
    }

    fun logIn(username: String, password: String): Boolean {
        if (true) { //TODO: Validate with google auth
            setCurrentUser(UserManager.findUserByUsername())
            return true
        }
        return false
    }

    companion object :
        SingletonHolder<SessionManager, Context>(::SessionManager)
}