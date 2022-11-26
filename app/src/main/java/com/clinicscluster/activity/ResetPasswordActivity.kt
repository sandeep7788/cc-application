package com.clinicscluster.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivityResetBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.ApplicationInit
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetBinding
    lateinit var mContext: Activity
    var TAG = "@@ResetPasswordActivity"
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset)


        onClick()
    }

    fun onClick() {
        binding.btnSubmit.setOnClickListener {
//            val mIntent = Intent(this@ResetPasswordActivity, SignInActivity::class.java)
//            startActivity(mIntent)
            Utility.showSnackBar(this@ResetPasswordActivity, "Please try later.")
        }
        binding.btnSignIn.setOnClickListener {
            val mIntent = Intent(this@ResetPasswordActivity, SignInActivity::class.java)
            startActivity(mIntent)
        }
    }
    fun callApi() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.login(
            binding.edtPassword.text.toString(),
            binding.edtPassword.text.toString(),
            "0"
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

//                            val obj: UserModel = Gson().fromJson(json.getJSONObject("user").toString(), UserModel::class.java)
                            ApplicationInit.writeUser(json.getJSONObject("user").toString())
                            ApplicationInit.setLogIn()
                            val mIntent = Intent(this@ResetPasswordActivity, DashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            mIntent.putExtra("isShowAptDashBoard",true)
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