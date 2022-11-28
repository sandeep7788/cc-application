package com.clinicscluster.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivityServiceBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.ServiceListModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityServiceBinding
    lateinit var mContext: Activity
    var TAG = "@@ServiceActivity"
    var progressDialog: SweetAlertDialog? = null
    var serviceId= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service)
        binding.toolbar.txtTitle.text = "Service"
        mContext = this@ServiceActivity
        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)

        if (intent.extras != null) {
            serviceId = intent.getIntExtra("id",0)
        } else {
            finish()
        }

        onClick()
        getServiceApi()

    }
    fun onClick() {
        binding.toolbar.imgBack.setOnClickListener { finish() }

    }

    fun getServiceApi() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.front_service_details(serviceId).enqueue(object :
            Callback<JsonObject> {
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
                            val JsonObjectData = json.getJSONObject("service")
                            val obj: ServiceListModel = Gson().fromJson(JsonObjectData.toString(), ServiceListModel::class.java)

                            Utility.setImage(mContext, obj.image, binding.image)

                            var title = Utility.html2text(obj.title)
                            var sDes = Utility.html2text(obj.shortDescription)
                            var des = Utility.html2text(obj.description)

                            binding.txtTitle.setText(title)
                            binding.txtShortDes.setText(sDes)
                            binding.txtLongDes.setText(des)

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
    fun html2text(html: String?): String? {
        return Jsoup.parse(html).text()
    }
}