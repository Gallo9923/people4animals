package com.example.people4animals.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        binding.createAccountBtn.setOnClickListener {
            createAccount()
        }

    }

    private fun createAccount() {
        val user = User(
            "",
            binding.email.editText!!.text.toString(),
            binding.firstName.editText!!.text.toString() + " " + binding.lastName.editText!!.text.toString(),
            binding.phoneNumber.editText!!.text.toString(),
            binding.city.selectedItem.toString()
        )

        if (validateUserData(user)) {
            UserManager.signUp(this, user, binding.password.editText!!.text.toString())
        }
    }

    private fun validateUserData(user: User): Boolean { //TODO: Info validation
        return true
    }
}