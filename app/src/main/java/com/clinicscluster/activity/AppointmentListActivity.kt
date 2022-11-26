package com.clinicscluster.activity

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.adapter.AppointmentListAdapter
import com.clinicscluster.databinding.ActivityAppointmentListBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.ApplicationInit
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.appointment.Appointment
import com.clinicscluster.model.appointment.AppointmentModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppointmentListActivity : AppCompatActivity() {
    lateinit var binding: ActivityAppointmentListBinding
    lateinit var mContext: Activity
    var TAG = "@@AppointmentListActivity"
    var progressDialog: SweetAlertDialog? = null
    var listService: ArrayList<Appointment> = ArrayList()
    var adapterService: AppointmentListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_list)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_list)
        binding.toolbar.txtTitle.text = "My Appointments"
        mContext = this@AppointmentListActivity
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        onClick()
        setList()
        getData()
    }

    fun onClick() {
        binding.toolbar.imgBack.setOnClickListener { finish() }

    }

    fun setList() {
        var linearLayoutManager: LinearLayoutManager? =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerService.layoutManager = linearLayoutManager
        binding.recyclerService.itemAnimator = DefaultItemAnimator()
        adapterService = AppointmentListAdapter(listService, mContext)
        binding.recyclerService.adapter = adapterService
    }
    fun getData() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        ApplicationInit.readUser()?.id?.let {
            apiInterface.myAppointments(it).enqueue(object : Callback<AppointmentModel> {
                override fun onFailure(call: Call<AppointmentModel>, t: Throwable) {
                    Toast.makeText(mContext, " " + t.message.toString(), Toast.LENGTH_LONG)
                        .show()
                    progressDialog!!.dismiss()
                }

                override fun onResponse(
                    call: Call<AppointmentModel>,
                    response: Response<AppointmentModel>
                ) {
                    progressDialog!!.dismiss()
                    try {
                        if (response.isSuccessful) {

                            Log.d(TAG, "onResponse: " + response.body().toString())


                            if (response.body()?.status != null && response.body()?.status == true && response.body()?.appointments?.isNotEmpty() == true) {
                                response.body()?.appointments?.let { listService.addAll(it) }
                                adapterService?.notifyDataSetChanged()

                            } else {
                                response.body()?.message?.let {
                                    Utility.showDialog(
                                        mContext,
                                        SweetAlertDialog.ERROR_TYPE, it
                                    )
                                }

                            }
                        } else {
                            Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                    }

                }

            })
        }
    }

}