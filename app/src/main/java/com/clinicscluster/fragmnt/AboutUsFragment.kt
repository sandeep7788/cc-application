package com.clinicscluster.fragmnt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.databinding.ActivityAboutUsBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.AboutModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutUsFragment : Fragment() {
    lateinit var binding: ActivityAboutUsBinding
    lateinit var mContext: Context
    var TAG = "@@AboutUsFragment"
    var progressDialog: SweetAlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.activity_about_us, container, false)
        mContext = container!!.context

        progressDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE)
        progressDialog!!.progressHelper.barColor = R.color.theme_color
        progressDialog!!.titleText = "Loading ..."
        progressDialog!!.setCancelable(false)


        getAboutUsApi()

        return binding.root
    }

    fun getAboutUsApi() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.aboutUs.enqueue(object :
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
                            val JsonObjectData = json.getJSONObject("about")
                            val obj: AboutModel =
                                Gson().fromJson(JsonObjectData.toString(), AboutModel::class.java)

                            Utility.setImage(mContext, obj.image, binding.image)
                            binding.txtTitle.text = obj.name
                            binding.txtShortDes.text = obj.metaDescription
                            binding.txtLongDes.text = obj.content

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
}