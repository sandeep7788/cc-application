package com.clinicscluster

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.clinicscluster.databinding.ActivityResetBinding
import com.clinicscluster.databinding.ActivitySignInBinding
import com.clinicscluster.helper.Utility

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset)


        onClick()
    }

    fun onClick() {
        binding.btnSubmit.setOnClickListener {
//            val mIntent = Intent(this@ResetPasswordActivity, SignInActivity::class.java)
//            startActivity(mIntent)
            Utility.showSnackBar(this@ResetPasswordActivity, "Please try later.")
        }
        binding.btnSignIn.setOnClickListener {
            val mIntent = Intent(this@ResetPasswordActivity, SignInActivity::class.java)
            startActivity(mIntent)
        }
    }

}