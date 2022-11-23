package com.clinicscluster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.clinicscluster.activity.AppointmentDashBoard
import com.clinicscluster.dashboard.HomeFragment
import com.clinicscluster.dashboard.ServiceFragment
import com.clinicscluster.databinding.ActivityDashboardBinding
import com.clinicscluster.helper.ApplicationInit
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    var FRAGMENT_OTHER = "FRAGMENT_OTHER"
    var TAG = "@@DashboardActivity"
    val RequestPermissionCode = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)


        onClick()
        init()
    }

    fun onClick() {
        binding.newAppointment.setOnClickListener {
            val mIntent:Intent
            if (ApplicationInit.isLogIn()) {
                mIntent = Intent(this@DashboardActivity, AppointmentDashBoard::class.java)
            } else {
                 mIntent = Intent(this@DashboardActivity, SignInActivity::class.java)
            }
            startActivity(mIntent)
        }

        binding.bottomNev.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemHome -> {
                    viewFragment(HomeFragment(), "FRAGMENT_HOME")
                }
                R.id.itemServices -> {
//                    viewFragment(PatientListFragment(), FRAGMENT_OTHER)
                    viewFragment(ServiceFragment(), FRAGMENT_OTHER)
                }

            }
            true
        })
    }

    fun init() {
        viewFragment(HomeFragment(), "FRAGMENT_HOME")
        binding.bottomNev.menu.getItem(2).isChecked = true

        if (ApplicationInit.isLogIn()) {
            binding.newAppointment.text = "DashBoard"
        } else {
            binding.newAppointment.text = "Book Appointment"
        }
    }

    private fun viewFragment(
        fragment: Fragment,
        name: String,
    ) {
        var fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        val count = fragmentManager.backStackEntryCount
        if (name == FRAGMENT_OTHER) {
            fragmentTransaction.addToBackStack(name)
        }
        fragmentTransaction.commit()
        fragmentManager.addOnBackStackChangedListener(object :
            FragmentManager.OnBackStackChangedListener {
            override fun onBackStackChanged() {
                if (fragmentManager.backStackEntryCount <= count) {
                    fragmentManager.popBackStack(
                        FRAGMENT_OTHER,
                        android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    fragmentManager.removeOnBackStackChangedListener(this)
                    binding.bottomNev.menu.getItem(2).isChecked = true


                }
                Log.e(TAG, "onBackPressed" + count)
            }
        })
    }

    var exitOpened = false
    override fun onBackPressed() {
        if (isTaskRoot && !exitOpened) {
            exitOpened = true
            Toast.makeText(this, "Please press back again to exit", Toast.LENGTH_SHORT).show()
            return
        }
        super.onBackPressed()
    }
}