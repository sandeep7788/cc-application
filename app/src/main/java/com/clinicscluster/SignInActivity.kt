package com.clinicscluster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.clinicscluster.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)

        onClick()

    }
    fun onClick() {
        binding.btnSignIn.setOnClickListener {
            val mIntent = Intent(this@SignInActivity,DashboardActivity::class.java)
            startActivity(mIntent)
        }
        binding.btnForgotPassword.setOnClickListener {
            val mIntent = Intent(this@SignInActivity,ForgotPasswordActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnSignUp.setOnClickListener {
            val mIntent = Intent(this@SignInActivity,RegistrationActivity::class.java)
            startActivity(mIntent)
        }
    }

}