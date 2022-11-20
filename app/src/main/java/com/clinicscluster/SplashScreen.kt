package com.clinicscluster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.clinicscluster.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)


//        startActivity(Intent(this, SignUpActivity::class.java))
        Handler().postDelayed(Runnable {
            val mainIntent = Intent(this@SplashScreen,DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            this@SplashScreen.startActivity(mainIntent)
            this@SplashScreen.finish()
        },3000)

    }
}