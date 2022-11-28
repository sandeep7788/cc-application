package com.clinicscluster.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivityRegistrationBinding
import com.clinicscluster.helper.*
import com.clinicscluster.model.BloodGroupModel
import com.clinicscluster.model.ClinicModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding
    lateinit var mContext: Activity
    var TAG = "@@RegistrationActivity"
    var progressDialog: SweetAlertDialog? = null
    lateinit var imageview: ImageView
    private val PICK_IMAGE: Int = 100
    var imageUri: Uri? = null
    var status: Boolean = false
    var RECORD_REQUEST_CODE = 101
    var date: Calendar = Calendar.getInstance()
    var thisAYear = date.get(Calendar.YEAR).toInt()
    var thisAMonth = date.get(Calendar.MONTH).toInt()
    var thisADay = date.get(Calendar.DAY_OF_MONTH).toInt()
    var listClinic: ArrayList<ClinicModel> = ArrayList()
    var listBloodGroup: ArrayList<BloodGroupModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration)
        binding.toolbar.txtTitle.text = "Registration"
        mContext = this@RegistrationActivity
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        onClick()

        getBloodGroupData()
        spinnerGender()
        getClinicData()
    }


    fun spinnerBloodGroup() {
        var item: ArrayList<String> = ArrayList()
        listBloodGroup.forEach {
            item.add(it.title)
        }

        val spinner = binding.edtBloodGroup
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

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }


    fun getBloodGroupData() {
        progressDialog!!.show()
        listBloodGroup.clear()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.getbloodgroups.enqueue(object : Callback<JsonObject> {
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
                        var jsonList = json.getJSONArray("bloodgroup")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val obj: BloodGroupModel = Gson().fromJson(JsonObjectData.toString(), BloodGroupModel::class.java)
                                listBloodGroup.add(obj)
                            }
                            spinnerBloodGroup()
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
                    progressDialog!!.dismiss()
                }

            }

        })
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
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
            },
            thisAYear,
            thisAMonth,
            thisADay
        )

        dpd.getDatePicker().maxDate = System.currentTimeMillis()
        dpd.datePicker.spinnersShown = true
        dpd.datePicker.calendarViewShown = false
        dpd.show()
    }

    fun onClick() {
        binding.toolbar.imgBack.setOnClickListener { finish() }

        binding.btnSubmit.setOnClickListener {

            if (!status || imageUri == null) {
                Utility.showSnackBar(mContext, "Please select your picture")
            } else if (binding.edtEmail.text.isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "EmailID")
            } else if (!binding.edtEmail.text.toString().isValidEmail()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "valid EmailID")
            } else if (binding.edtPassword.text.isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "password")
            } else if (binding.edtPassword.text.length < 8) {
                Utility.showSnackBar(mContext, "password length should be minimum Eight")
            } else if (!binding.edtPassword.text.toString().equals(binding.edtConfirmPassword.text.toString())) {
                Utility.showSnackBar(mContext,"new password and confirm password are not same.")
            } else if (binding.edtFNam.text.toString().isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "First name")
            } else if (binding.txtlName.text.toString().isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "Last name")
            } else if (binding.edtPhone.text.toString().isEmpty() || binding.edtPhone.text.length < 9) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "valid Phone number")
            } else if (binding.edtDob.text.toString().isEmpty() || binding.edtDob.text.length < 5) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "Date of Brith")
            }  else if (listClinic.isEmpty()) {
                Utility.showSnackBar(mContext, "Please select clinic")
            } else if (listBloodGroup.isEmpty()) {
            Utility.showSnackBar(mContext, "Please select blood group")
            } else if (binding.edtCity.text.toString().isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "City")
            } else if (binding.edtCountry.text.toString().isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "Country")
            } else if (binding.edtPostCode.text.toString().isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "Postal Code")
            } else if (binding.edtAddress.text.toString().isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "Address")
            } else {
                userRegisterApi()
            }
        }

        binding.edtDob.setOnClickListener {
            setDate(binding.edtDob)
        }

        binding.txtSignIn.setOnClickListener {
            val mIntent = Intent(this@RegistrationActivity, SignInActivity::class.java)
            startActivity(mIntent)
            finish()
        }

        binding.imgEdit.setOnClickListener {

            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            {
                makeRequest()
            }else {
                openGallery()
            }
        }
    }

    fun spinnerClinic() {
        var item: ArrayList<String> = ArrayList()
        listClinic.forEach {
            item.add(it.name)
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
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
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
                            spinnerClinic()
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
                    progressDialog!!.dismiss()
                }

            }

        })
    }

    fun makeRequest() {
        ActivityCompat.requestPermissions(
            this@RegistrationActivity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            RECORD_REQUEST_CODE
        )
    }

    @NonNull
    private fun createPartFromString(value: String): RequestBody? {
        return RequestBody.create(
            MultipartBody.FORM, value
        )
    }

    fun spinnerGender() {
        val item = resources.getStringArray(R.array.gender)

        val spinner = binding.spinnerGender
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

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
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
            val mIntent = Intent(this@RegistrationActivity, SignInActivity::class.java)
            startActivity(mIntent)
            finish()
        }
        pDialog.show()
    }


    fun userRegisterApi() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        val file = File(FileUtils.getPath(mContext, imageUri))
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val parts =
            MultipartBody.Part.createFormData("profile_pic", file.name, requestFile)

        val fname = createPartFromString(binding.edtFNam.text.toString())
        val lname = createPartFromString(binding.txtlName.text.toString())
        val edtEmail = createPartFromString(binding.edtEmail.text.toString())
        val password = createPartFromString(binding.edtPassword.text.toString())
        val phone = createPartFromString(binding.edtPhone.text.toString())
        val dob = createPartFromString(binding.edtDob.text.toString())
        val bloodGroup = createPartFromString(binding.edtBloodGroup.selectedItem.toString())
        val clinicId = createPartFromString(listClinic.get(binding.spinnerClinic.selectedItemPosition).id.toString())
        val gender = createPartFromString(binding.spinnerGender.selectedItem.toString())
        val edtPostCode = createPartFromString(binding.edtPostCode.text.toString())
        val edtAddress = createPartFromString(binding.edtAddress.text.toString())
        val country = createPartFromString(binding.edtCountry.text.toString())
        val city = createPartFromString(binding.edtCity.text.toString())

        apiInterface.userregister(
            fname,lname,edtEmail,password,phone,dob,bloodGroup,clinicId,gender,edtPostCode,edtAddress,country,city,parts
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


                            showDialog(json.getString("message") + " ")
//                            val obj: UserModel = Gson().fromJson(json.getJSONObject("user").toString(), UserModel::class.java)
//                            ApplicationInit.writeUser(json.getJSONObject("user").toString())

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2 || requestCode == PICK_IMAGE) {
                val selectedImage = data!!.data
                val filePath =
                    arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor? =
                    contentResolver.query(selectedImage!!, filePath, null, null, null)
                c!!.moveToFirst()
                val columnIndex: Int = c.getColumnIndex(filePath[0])
                val picturePath: String = c.getString(columnIndex)
                c.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                binding.imgProfile.setImageBitmap(thumbnail)
                imageUri = data.data!!
                status = true

            }
        }
    }

    open fun openGallery(): Unit {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    private fun selectImage() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder =
            AlertDialog.Builder(this@RegistrationActivity)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = File(
                    Environment.getExternalStorageDirectory(),
                    "temp.jpg"
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                startActivityForResult(intent, 1)
            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onDestroy() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
        this@RegistrationActivity.onVisibleBehindCanceled()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("@@permission", "Permission has been denied by user")
//                    makeRequest()
                    dialog()
                } else {
                    Log.i("@@permission", "Permission has been granted by user")
                }
            }
        }
    }

    fun dialog() {
        val builder =
            AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT)
        builder.setTitle("Need Permission")
        builder.setMessage("This app needs storage permission.")
        builder.setPositiveButton("Grant", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0?.cancel()
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

        })
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0!!.cancel()
            }

        })
        builder.show()
    }

}