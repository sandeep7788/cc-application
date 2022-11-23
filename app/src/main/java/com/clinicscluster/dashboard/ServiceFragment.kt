package com.clinicscluster.dashboard

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
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.adapter.ReviewAdapter
import com.clinicscluster.adapter.ServiceListFragmentAdapter
import com.clinicscluster.adapter.ViewPagerAdapter
import com.clinicscluster.databinding.FragmentServiceBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.ServiceListModel
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ServiceFragment : Fragment() {

    var listBanner: ArrayList<String> = ArrayList()
    lateinit var binding: FragmentServiceBinding
    lateinit var thiscontext: Context
    var mViewPagerAdapter: ViewPagerAdapter? = null
    var mReviewAdapter: ReviewAdapter? = null
    var TAG = "@@ServiceFragment"
    var progressDialog: SweetAlertDialog? = null
    var listService: ArrayList<ServiceListModel> = ArrayList()
    var adapterService: ServiceListFragmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
//        var view=inflater.inflate(com.foamkart.R.layout.fragment_home, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)
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

        setServiceList()
        getData()

    }

    fun setServiceList() {
        var linearLayoutManager: LinearLayoutManager? =
            LinearLayoutManager(thiscontext, LinearLayoutManager.VERTICAL, false)
        binding.recyclerService.layoutManager = linearLayoutManager
        binding.recyclerService.itemAnimator = DefaultItemAnimator()
        adapterService = ServiceListFragmentAdapter(listService, thiscontext)
        binding.recyclerService.adapter = adapterService
    }

    fun getData() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.frontService.enqueue(object : Callback<JsonObject> {
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
                        var jsonList = json.getJSONArray("services")

                        if (json.getBoolean("status") != null && json.getBoolean("status")) {
                            for (i in 0 until jsonList.length()) {
                                val JsonObjectData = jsonList.getJSONObject(i)
                                val data = ServiceListModel()
                                data.title = (JsonObjectData.getString("title"))
                                data.shortDescription =
                                    (JsonObjectData.getString("short_description"))
                                data.image = (JsonObjectData.getString("image"))
                                data.description = (JsonObjectData.getString("description"))
                                listService.add(data)
                            }


                            if (listBanner.isEmpty()) {
                                binding.tabViewpager.visibility = View.GONE
                            }
                            adapterService?.notifyDataSetChanged()
                            mViewPagerAdapter?.notifyDataSetChanged()

                        } else {
                            Utility.showDialog(
                                thiscontext,
                                SweetAlertDialog.ERROR_TYPE, json.getString("message")
                            )

                        }
                    } else {
                        Toast.makeText(thiscontext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(thiscontext, "Something went wrong! ", Toast.LENGTH_LONG).show()
                }

            }

        })
    }

}