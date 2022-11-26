package com.clinicscluster.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivitySignInBinding
import com.clinicscluster.helper.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var mContext: Activity
    var TAG = "@@SignInActivity"
    var progressDialog: SweetAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        mContext = this@SignInActivity
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        onClick()

    }

    fun onClick() {
        binding.btnSignIn.setOnClickListener {

            if (binding.edtEmail.text.isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "EmailID")
            } else if (!binding.edtEmail.text.toString().isValidEmail()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "valid EmailID")
            } else if (binding.edtPassword.text.isEmpty()) {
                Utility.showSnackBar(mContext, ApiContants.errorMsg + "password")
            } else loginApi()

        }

        binding.btnForgotPassword.setOnClickListener {
            val mIntent = Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
            startActivity(mIntent)
        }

        binding.btnSignUp.setOnClickListener {
            val mIntent = Intent(this@SignInActivity, RegistrationActivity::class.java)
            startActivity(mIntent)
        }
    }
    fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    fun loginApi() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.login(
            binding.edtEmail.text.toString(),
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
                            val mIntent = Intent(this@SignInActivity, DashboardActivity::class.java)
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