package com.clinicscluster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.clinicscluster.databinding.ActivityDashboardBinding
import com.clinicscluster.databinding.ActivitySplashScreenBinding

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)


        onClick()
    }

    fun onClick() {
        binding.newAppointment.setOnClickListener {
            val mIntent = Intent(this@DashboardActivity,SignInActivity::class.java)
            startActivity(mIntent)
        }
    }
}