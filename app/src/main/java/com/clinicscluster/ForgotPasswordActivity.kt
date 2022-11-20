package com.clinicscluster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.clinicscluster.databinding.ActivityForgotBinding
import com.clinicscluster.databinding.ActivitySignInBinding

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot)


        onClick()
    }

    fun onClick() {
        binding.btnSignIn.setOnClickListener {
            val mIntent = Intent(this@ForgotPasswordActivity,SignInActivity::class.java)
            startActivity(mIntent)
        }
        binding.btnSend.setOnClickListener {
            val mIntent = Intent(this@ForgotPasswordActivity,ResetPasswordActivity::class.java)
            startActivity(mIntent)
        }
    }
}