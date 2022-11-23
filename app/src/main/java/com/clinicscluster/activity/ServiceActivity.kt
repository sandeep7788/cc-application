package com.clinicscluster.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.adapter.AppointmentListAdapter
import com.clinicscluster.databinding.ActivityAppointmentListBinding
import com.clinicscluster.databinding.ActivityServiceBinding
import com.clinicscluster.model.appointment.Appointment

class ServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceBinding
    lateinit var mContext: Activity
    var TAG = "@@AppointmentListActivity"
    var progressDialog: SweetAlertDialog? = null
    var listService: ArrayList<Appointment> = ArrayList()
    var adapterService: AppointmentListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service)
        binding.toolbar.txtTitle.text = "Services"
        mContext = this@ServiceActivity
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        onClick()

    }
    fun onClick() {
        binding.toolbar.imgBack.setOnClickListener { finish() }

    }
}