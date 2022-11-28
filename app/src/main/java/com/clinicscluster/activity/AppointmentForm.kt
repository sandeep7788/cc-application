package com.clinicscluster.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.adapter.AptCheckBoxAdapter
import com.clinicscluster.databinding.ActivityAppointmentFormBinding
import com.clinicscluster.helper.*
import com.clinicscluster.model.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AppointmentForm : AppCompatActivity() {
    lateinit var binding: ActivityAppointmentFormBinding
    lateinit var mContext: Activity
    var TAG = "@@AppointmentForm"
    var progressDialog: SweetAlertDialog? = null
    var listClinic: ArrayList<ClinicModel> = ArrayList()
    var listService: ArrayList<ServiceModel> = ArrayList()
    var listDoctor: ArrayList<DoctorModel> = ArrayList()
    var listTimeslot: ArrayList<TimeSlotModel> = ArrayList()
    var listCheckServices = ""
    var date: Calendar = Calendar.getInstance()
    var thisAYear = date.get(Calendar.YEAR).toInt()
    var thisAMonth = date.get(Calendar.MONTH).toInt()
    var thisADay = date.get(Calendar.DAY_OF_MONTH).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_form)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appointment_form)
        binding.toolbar.txtTitle.text = "Registration"
        mContext = this@AppointmentForm
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        onClick()


        setServiceList()
        getClinicData()
        getServicesApi()

    }

    fun setDate(txtView: TextView) {
        Utility.hideKeyboard(this)
        val dpd = DatePickerDialog(
            this,
            R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { view2, thisYear, thisMonth, thisDay ->
                val calendar = Calendar.getInstance()
                calendar[thisYear, thisMonth] = thisDay

                val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                val dateString = dateFormat.format(calendar.getTime())

                txtView.text = dateString

                if (listDoctor.isNotEmpty() && listClinic.isNotEmpty()) {
                    getTimeSlotApi()
                }
            },
            thisAYear,
            thisAMonth,
            thisADay
        )
        dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
        dpd.datePicker.spinnersShown = true
        dpd.datePicker.calendarViewShown = false
        dpd.show()
    }



    fun removeLastChar(str: String): String? {
        return removeLastChars(str, 1)
    }

    fun removeLastChars(str: String, chars: Int): String? {
        return str.substring(0, str.length - chars)
    }

    fun onClick() {



        binding.toolbar.imgBack.setOnClickListener { finish() }

        binding.edtDate.setOnClickListener {
            setDate(binding.edtDate)
        }

        binding.btnSubmit.setOnClickListener {
            listCheckServices = ""
            listService.forEach {
                if (it.isChecked) {
                    listCheckServices = listCheckServices + it.id.toString() + ","
                }
            }
            if (listCheckServices.isNotEmpty())
                listCheckServices = removeLastChar(listCheckServices.toString()).toString()

            if (binding.edtDate.text.toString().isEmpty() || binding.edtDate.text.length < 5) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "Date of Appointment")
            } else if (listClinic.isEmpty()) {
                Utility.showSnackBar(mContext, "Please select clinic")
            }  else if (listDoctor.isEmpty()) {
                Utility.showSnackBar(mContext, "Please select doctor")
            } else if (listCheckServices.isEmpty()) {
                Utility.showSnackBar(mContext, "Please select service")
            } else if(!containsDigit(listTimeslot.get(binding.spinnerTimeSlot.selectedItemPosition).option)) {
                Utility.showSnackBar(mContext, "Please select valid time slot")
            }else if (listTimeslot.isEmpty()) {
                Utility.showSnackBar(mContext, "Please select time slot")
            }else {
                add_appointmentsApi()
            }
        }



    }

    fun spinnerClinic() {
        var item: ArrayList<String> = ArrayList()
        listClinic.forEach {
            item.add(Utility.html2text(it.name).toString())
        }

        val spinner = binding.spinnerClinic
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                R.layout.simple_spinner_item, item
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long,
                ) {
                    getDoctorApi()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    var adapterDoctor : ArrayAdapter<String>?= null
    var adapterTimeSlot : ArrayAdapter<String>?= null

    fun spinnerDoctor() {
        var item: ArrayList<String> = ArrayList()
        listDoctor.forEach {
            item.add(Utility.html2text(it.firstName +" "+ it.lastName).toString())
        }

        val spinner = binding.spinnerDoctor
        if (spinner != null) {
            adapterDoctor = ArrayAdapter(
                this,
                R.layout.simple_spinner_item, item
            )
            spinner.adapter = adapterDoctor

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long,
                ) {
                    getTimeSlotApi()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
    fun spinnerTimeSlot() {
        var item: ArrayList<String> = ArrayList()
        listTimeslot.forEach {
            item.add(Utility.html2text(it.option).toString())
        }

        val spinner = binding.spinnerTimeSlot
        if (spinner != null) {
            adapterTimeSlot = ArrayAdapter(
                this,
                R.layout.simple_spinner_item, item
            )
            spinner.adapter = adapterTimeSlot

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long,
                ) {
                    var status = false
                    if (listTimeslot.isNotEmpty()) {

                        for (i in 0 until listTimeslot.size) {
                            if (containsDigit(listTimeslot.get(i).option)){
                                status = true
                                break
                            }
                        }
                    }
                    if (!status) {
                        Utility.showSnackBar(mContext, "please select another Doctor, time slot not available.")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
    fun containsDigit(aString: String?): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aString != null && !aString.isEmpty() &&
                    aString.chars().anyMatch { codePoint: Int ->
                        Character.isDigit(
                            codePoint
                        )
                    }
        } else {
            return containsDigitTwo(aString)
        }
    }
    fun containsDigitTwo(aString: String?): Boolean {
        if (aString != null && !aString.isEmpty()) {
            for (c in aString.toCharArray()) {
                if (Character.isDigit(c)) {
                    return true
                }
            }
        }
        return false
    }
    fun getClinicData() {
        progressDialog!!.show()
        listClinic.clear()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.clinic.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(mContext, " " + t.message.toString(), Toast.LENGTH_LONG)
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
                        var jsonList = json.getJSONArray("clinics")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val obj: ClinicModel = Gson().fromJson(JsonObjectData.toString(), ClinicModel::class.java)
                                listClinic.add(obj)
                            }


                            if (listClinic.isNotEmpty()) {
                                spinnerClinic()
                                getServicesApi()
                            } else {
                                listDoctor.clear()
                                listTimeslot?.clear()
                                Utility.showSnackBar(mContext, "clinic not available.")
                                binding.spinnerDoctor.adapter = null
                                binding.spinnerTimeSlot.adapter = null
                            }

                        } else {
                            Utility.showDialog(
                                mContext,
                                SweetAlertDialog.ERROR_TYPE, json.getString("message")
                            )

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

    fun getDoctorApi() {
        listDoctor.clear()
        binding.progressBarDoctor.visibility = View.VISIBLE
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.getdoctorsbyclinic(listClinic.get(binding.spinnerClinic.selectedItemPosition).id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(mContext, " " + t.message.toString(), Toast.LENGTH_LONG)
                    .show()
                binding.progressBarDoctor.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                binding.progressBarDoctor.visibility = View.GONE
                try {
                    if (response.isSuccessful) {

                        Log.d(TAG, "onResponse: " + response.body().toString())
                        val json: JSONObject = JSONObject(response.body().toString())
                        var jsonList = json.getJSONArray("doctors")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val obj: DoctorModel = Gson().fromJson(JsonObjectData.toString(), DoctorModel::class.java)
                                listDoctor.add(obj)
                            }

                            if (listDoctor.isNotEmpty()) {
                                spinnerDoctor()
                                getTimeSlotApi()
                            } else {
                                listDoctor.clear()
                                listTimeslot?.clear()
                                binding.spinnerDoctor.adapter = null
                                binding.spinnerTimeSlot.adapter = null

                                Utility.showSnackBar(mContext, "doctors not available in this clinic.")
                            }


                        } else {
                            Utility.showDialog(
                                mContext,
                                SweetAlertDialog.ERROR_TYPE, json.getString("message")
                            )

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

    var counter = 0
    fun getTimeSlotApi() {

        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        if (binding.edtDate.text.toString().isEmpty()) {

            if(counter > 1) {
                Toast.makeText(mContext, "Please select Appointment date.", Toast.LENGTH_LONG)
                    .show()
                counter++
            }
            return
        }

        var docId = 0

        try {
            docId = listDoctor.get(binding.spinnerDoctor.selectedItemPosition).id
        } catch (e:Exception) {
            Log.e(TAG, "getTimeSlotApi: "+e.message)
            getDoctorApi()
        }
        binding.progressBarTimeSlot.visibility = View.VISIBLE
        apiInterface.getslotbydoctor(docId,binding.edtDate.text.toString()).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(mContext, " " + t.message.toString(), Toast.LENGTH_LONG)
                    .show()
                binding.progressBarTimeSlot.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                binding.progressBarTimeSlot.visibility = View.GONE
                listTimeslot.clear()
                try {
                    if (response.isSuccessful) {

                        Log.d(TAG, "onResponse: " + response.body().toString())
                        val json: JSONObject = JSONObject(response.body().toString())
                        var jsonList = json.getJSONArray("timeslot")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val obj: TimeSlotModel = Gson().fromJson(JsonObjectData.toString(), TimeSlotModel::class.java)
                                listTimeslot.add(obj)
                            }

                            spinnerTimeSlot()
                        } else {
                            Utility.showDialog(
                                mContext,
                                SweetAlertDialog.ERROR_TYPE, json.getString("message")
                            )

                        }
                    } else {
                        Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                    binding.progressBarTimeSlot.visibility = View.GONE
                }

            }

        })
    }

    fun getServicesApi() {
        progressDialog!!.show()
        listService.clear()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.services.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(mContext, " " + t.message.toString(), Toast.LENGTH_LONG)
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
                        var jsonList = json.getJSONArray("services")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val obj: ServiceModel = Gson().fromJson(JsonObjectData.toString(), ServiceModel::class.java)
                                listService.add(obj)
                            }
                            adapterService?.notifyDataSetChanged()
                        } else {
                            Utility.showDialog(
                                mContext,
                                SweetAlertDialog.ERROR_TYPE, json.getString("message")
                            )

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


    fun add_appointmentsApi() {
        progressDialog!!.show()

        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

       try {
           apiInterface.add_appointments(
               ApplicationInit.readUser()?.id.toString(),
               binding.edtDate.text.toString(),
               listClinic.get(binding.spinnerClinic.selectedItemPosition).id,
               listDoctor.get(binding.spinnerDoctor.selectedItemPosition).id,
               listCheckServices,
               listTimeslot.get(binding.spinnerTimeSlot.selectedItemPosition).value,
               binding.edtNote.text.toString()
           ).enqueue(object : Callback<JsonObject> {
               override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                   Toast.makeText(mContext, " " + t.message.toString(), Toast.LENGTH_LONG)
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

                           if (json.getBoolean("status") != null && json.getBoolean("status")) {
                               showDialog(json.getString("message")+ " ")
                           } else {
                               Utility.showDialog(
                                   mContext,
                                   SweetAlertDialog.ERROR_TYPE, json.getString("message")
                               )

                           }
                       } else {
                           Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                       }
                   } catch (e: Exception) {
                       Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                   }

               }

           })
       } catch (e:Exception) {
           Log.e(TAG, "add_appointmentsApi: "+e.message )
           Toast.makeText(mContext, "Something went wrong! ", Toast.LENGTH_LONG).show()
       }


    }
    private fun showDialog(msg:String) {
        val pDialog =
            SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.titleText = msg
        pDialog.confirmText = "OK"
        pDialog.progressHelper.barColor = Color.parseColor("#FFE8560D")
        pDialog.setCancelable(false)
        pDialog.setConfirmClickListener {
            pDialog.dismiss()
            finish()
        }
        pDialog.show()
    }


    var adapterService: AptCheckBoxAdapter? = null

    fun setServiceList() {
        var linearLayoutManager: LinearLayoutManager? =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerService.layoutManager = linearLayoutManager
        binding.recyclerService.itemAnimator = DefaultItemAnimator()
        adapterService = AptCheckBoxAdapter(listService, mContext)
        binding.recyclerService.adapter = adapterService
    }
}