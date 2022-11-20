package com.clinicscluster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.clinicscluster.databinding.ActivityRegistrationBinding
import com.clinicscluster.databinding.ActivityResetBinding

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        binding.toolbar.txtTitle.setText("Registration")
        onClick()
    }

    fun onClick() {
        binding.toolbar.imgBack.setOnClickListener { finish() }
        binding.btnSubmit.setOnClickListener {
            val mIntent = Intent(this@RegistrationActivity, DashboardActivity::class.java)
            startActivity(mIntent)
        }
        binding.btnSubmit.setOnClickListener {
            val mIntent = Intent(this@RegistrationActivity, SignInActivity::class.java)
            startActivity(mIntent)
        }
    }
}