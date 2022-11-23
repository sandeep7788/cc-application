package com.clinicscluster.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.clinicscluster.DashboardActivity
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivityAppointmentDashboardBinding
import com.clinicscluster.databinding.ActivitySplashScreenBinding
import com.clinicscluster.helper.ApplicationInit

class AppointmentDashBoard : AppCompatActivity() {
    lateinit var binding: ActivityAppointmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_dashboard)
        binding.toolbar.txtTitle.text = "DashBoard"

        binding.toolbar.imgBack.setOnClickListener {
            val mIntent = Intent(this@AppointmentDashBoard, DashboardActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnMyApt.setOnClickListener {
            val mIntent = Intent(this@AppointmentDashBoard, AppointmentListActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnBookApt.setOnClickListener {
            val mIntent = Intent(this@AppointmentDashBoard, AppointmentForm::class.java)
            startActivity(mIntent)
        }
        binding.btnEditProfile.setOnClickListener {
            val mIntent = Intent(this@AppointmentDashBoard, ProfileActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes"
                ) { dialog, id ->             ApplicationInit.logout(true) }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onBackPressed() {
        val mIntent = Intent(this@AppointmentDashBoard, DashboardActivity::class.java)
        startActivity(mIntent)
    }
}