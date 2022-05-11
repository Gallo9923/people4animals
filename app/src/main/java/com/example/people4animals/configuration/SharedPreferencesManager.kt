package com.example.people4animals.configuration

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val sharedPreference: SharedPreferencesManager by lazy {
        SharedPreferencesManager(context)
    }
    private val sharedPreferencesName: String = "People4AnimalsPrefs"
    private val preferences: SharedPreferences

    var currentUser: String?
        get() = preferences.getString(sharedPreferencesName, "non-user")
        set(value) = preferences.edit().putString(sharedPreferencesName, value).apply()

    init {
        preferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    companion object :
        SingletonHolder<SharedPreferencesManager, Context>(::SharedPreferencesManager)
}
