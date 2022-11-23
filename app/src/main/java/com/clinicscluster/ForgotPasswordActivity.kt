package com.clinicscluster

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.databinding.ActivityForgotBinding
import com.clinicscluster.databinding.ActivitySignInBinding
import com.clinicscluster.helper.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotBinding
    lateinit var mContext: Activity
    var TAG = "@@HomeFragment"
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot)
        mContext = this@ForgotPasswordActivity
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        onClick()
    }

    fun onClick() {
        binding.btnSignIn.setOnClickListener {
            val mIntent = Intent(this@ForgotPasswordActivity,SignInActivity::class.java)
            startActivity(mIntent)
        }
        binding.btnSend.setOnClickListener {
                if (binding.edtEmail.text.isEmpty()) {
                    Utility.showSnackBar(mContext, ApiContants.errorMsg + "EmailID")
                } else if (!binding.edtEmail.text.toString().isValidEmail()) {
                    Utility.showSnackBar(mContext, ApiContants.errorMsg + "valid EmailID")
                } else {
                forgotApi()
            }
        }
    }

    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    fun forgotApi() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.forget_password(
            binding.edtEmail.text.toString()).enqueue(object : Callback<JsonObject> {
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
                        if (json.getString("message") != null){
                            Utility.showSnackBar(mContext,json.getString("message"))
                        }

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {

//                            val obj: UserModel = Gson().fromJson(json.getJSONObject("user").toString(), UserModel::class.java)

                            val mIntent = Intent(this@ForgotPasswordActivity,ResetPasswordActivity::class.java)
                            startActivity(mIntent)
                            finish()
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

}