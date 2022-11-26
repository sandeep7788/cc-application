package com.clinicscluster.fragmnt

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.adapter.DoctorDashBoardListAdapter
import com.clinicscluster.databinding.FragmentDoctorBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.DoctorModel
import com.clinicscluster.model.dashboard.Doctor
import com.clinicscluster.model.dashboard.ServiceListModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DoctorFragment : Fragment() {

    lateinit var binding: FragmentDoctorBinding
    lateinit var thiscontext: Context
    var TAG = "@@DoctorFragment"
    var progressDialog: SweetAlertDialog? = null
    var adapterDoctor: DoctorDashBoardListAdapter? = null
    var listDoctor: java.util.ArrayList<Doctor> = java.util.ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_doctor, container, false)
        thiscontext = container!!.context

        progressDialog = SweetAlertDialog(thiscontext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)
        set_ClickListener()

        init()

        return binding.root
    }

    @SuppressLint("WrongConstant")
    fun set_ClickListener() {

    }

    fun init() {

        setDoctorList()
        getData()

    }

    fun setDoctorList() {

        val linearLayoutManager = GridLayoutManager(thiscontext, 2)
        binding.recyclerDoctor.layoutManager = linearLayoutManager
        binding.recyclerDoctor.itemAnimator = DefaultItemAnimator()
        adapterDoctor = DoctorDashBoardListAdapter(listDoctor, thiscontext)
        binding.recyclerDoctor.adapter = adapterDoctor
    }

    fun getData() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.doctors.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(thiscontext, " " + t.message.toString(), Toast.LENGTH_LONG)
                    .show()
                progressDialog!!.dismiss()
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response.isSuccessful) {

                        Log.d(TAG, "onResponse: " + response.body().toString())
                        val json: JSONObject = JSONObject(response.body().toString())
                        var jsonList = json.getJSONArray("doctors")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val obj: Doctor = Gson().fromJson(JsonObjectData.toString(), Doctor::class.java)
                                listDoctor.add(obj)
                            }

                            adapterDoctor?.notifyDataSetChanged()

                        } else {
                            Utility.showDialog(
                                thiscontext,
                                SweetAlertDialog.ERROR_TYPE, json.getString("message")
                            )

                        }
                    } else {
                        Toast.makeText(thiscontext, "Something went wrong! ", Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(thiscontext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                    progressDialog!!.dismiss()
                }

            }

        })
    }

}