package com.clinicscluster.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivityDashboardBinding
import com.clinicscluster.fragmnt.*
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

        if (intent.extras !=null) {
            if (intent.getBooleanExtra("isShowAptDashBoard",false)) {
                viewFragment(DashBoardAptFragment(), FRAGMENT_OTHER, R.id.item_appointment)
            }
        }
    }

    fun onClick() {
        binding.newAppointment.setOnClickListener {
            if (ApplicationInit.isLogIn()) {
                viewFragment(DashBoardAptFragment(), FRAGMENT_OTHER, R.id.item_appointment)
            } else {
                startActivity(Intent(this@DashboardActivity, SignInActivity::class.java))
            }
        }

        binding.bottomNev.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.itemHome -> {
                    viewFragment(HomeFragment(), "FRAGMENT_HOME" , R.id.itemHome)
                }
                R.id.itemServices -> {
                    viewFragment(ServiceFragment(), FRAGMENT_OTHER, R.id.itemServices)
                }
                R.id.itemAboutUs -> {
                    viewFragment(AboutUsFragment(), FRAGMENT_OTHER, R.id.itemAboutUs)
                }
                R.id.item_doctor -> {
                    viewFragment(DoctorFragment(), FRAGMENT_OTHER, R.id.item_doctor)
                }
                R.id.item_appointment -> {
                    if (ApplicationInit.isLogIn()) {
                        viewFragment(DashBoardAptFragment(), FRAGMENT_OTHER, R.id.item_appointment)
                    } else startActivity(Intent(this@DashboardActivity,SignInActivity::class.java))
                }
            }
            true
        })
    }

    fun init() {
        viewFragment(HomeFragment(), "FRAGMENT_HOME", R.id.itemHome)
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
        id: Int
    ) {

        binding.bottomNev.getMenu().findItem(id).setChecked(true)

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
                    binding.bottomNev.menu.getItem(0).isChecked = true


                }
                Log.e(TAG, "onBackPressed" + count)
            }
        })
    }

//    var exitOpened = false
//    override fun onBackPressed() {
//        if (isTaskRoot && !exitOpened) {
//            exitOpened = true
//            Toast.makeText(this, "Please press back again to exit", Toast.LENGTH_SHORT).show()
//            return
//        }
//        super.onBackPressed()
//    }
}