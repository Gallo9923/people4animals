package com.example.people4animals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.example.people4animals.databinding.ActivityLogInViewBinding
import com.example.people4animals.databinding.ActivitySignUpBinding
import com.example.people4animals.domain.user.manager.UserManager
import com.example.people4animals.domain.user.model.User

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }


    fun createAccount(view: View) {
        var user = User(
            binding.email.text.toString(),
            binding.firstName.text.toString() + " " + binding.lastName.text.toString(),
            binding.phoneNumber.text.toString(),
            binding.city.selectedItem.toString()
        )

        if (validateUserData(user)) {
            UserManager.signUp(user, binding.password.text.toString())
        }
    }

    private fun validateUserData(user: User): Boolean { //TODO: Info validation
        return true
    }
}