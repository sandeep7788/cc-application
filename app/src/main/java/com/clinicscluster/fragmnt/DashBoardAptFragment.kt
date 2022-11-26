package com.clinicscluster.fragmnt

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.activity.AppointmentForm
import com.clinicscluster.activity.AppointmentListActivity
import com.clinicscluster.activity.ProfileActivity
import com.clinicscluster.databinding.ActivityAppointmentDashboardBinding
import com.clinicscluster.helper.ApplicationInit

class DashBoardAptFragment : Fragment() {
    lateinit var binding: ActivityAppointmentDashboardBinding
    lateinit var mContext: Context
    var TAG = "@@DashBoardAptFragment"
    var progressDialog: SweetAlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_appointment_dashboard, container, false)
        mContext = container!!.context

        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        init()


        return binding.root
    }

    fun init() {

        binding.btnMyApt.setOnClickListener {
            val mIntent = Intent(mContext, AppointmentListActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnBookApt.setOnClickListener {
            val mIntent = Intent(mContext, AppointmentForm::class.java)
            startActivity(mIntent)
        }
        binding.btnEditProfile.setOnClickListener {
            val mIntent = Intent(mContext, ProfileActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(mContext)
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes"
                ) { dialog, id ->             ApplicationInit.logout(true) }
                .setNegativeButton("No", null)
                .show()
        }
    }
}