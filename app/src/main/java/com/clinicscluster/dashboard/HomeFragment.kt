package com.clinicscluster.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.clinicscluster.R
import com.clinicscluster.adapter.DoctorDashBoardListAdapter
import com.clinicscluster.adapter.ReviewAdapter
import com.clinicscluster.adapter.ServiceDashBoardListAdapter
import com.clinicscluster.adapter.ViewPagerAdapter
import com.clinicscluster.databinding.FragmentHomeBinding
import com.clinicscluster.helper.ApiInterface
import com.clinicscluster.helper.RetrofitManager
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.DashboardModel
import com.clinicscluster.model.dashboard.Doctor
import com.clinicscluster.model.dashboard.Review
import com.clinicscluster.model.dashboard.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeFragment : Fragment() {

    var listDoctor: ArrayList<Doctor> = ArrayList()
    var listBanner: ArrayList<String> = ArrayList()
    var listReview: ArrayList<Review> = ArrayList()
    var linearLayoutManager: LinearLayoutManager? = null
    lateinit var binding: FragmentHomeBinding
    lateinit var thiscontext: Context
    var mViewPagerAdapter: ViewPagerAdapter? = null
    var mReviewAdapter: ReviewAdapter? = null
    var TAG = "@@HomeFragment"
    var progressDialog: SweetAlertDialog? = null
    var listService: ArrayList<Service> = ArrayList()
    var adapterService: ServiceDashBoardListAdapter? = null
    var adapterDoctor: DoctorDashBoardListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
//        var view=inflater.inflate(com.foamkart.R.layout.fragment_home, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
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
        setDoctorList()
        setBanner()
        setReview()
        getData()

    }

    fun setServiceList() {
        var linearLayoutManager: LinearLayoutManager? =
            LinearLayoutManager(thiscontext, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerService.layoutManager = linearLayoutManager
        binding.recyclerService.itemAnimator = DefaultItemAnimator()
        adapterService = ServiceDashBoardListAdapter(listService, thiscontext)
        binding.recyclerService.adapter = adapterService
    }

    fun setDoctorList() {

        val linearLayoutManager = GridLayoutManager(thiscontext, 2)
        binding.recyclerDoctor.layoutManager = linearLayoutManager
        binding.recyclerDoctor.itemAnimator = DefaultItemAnimator()
        adapterDoctor = DoctorDashBoardListAdapter(listDoctor, thiscontext)
        binding.recyclerDoctor.adapter = adapterDoctor
    }

    var currentPage = 0
    var currentPageTwo = 0
    var timer: Timer? = null
    var timerTwo: Timer? = null
    val DELAY_MS: Long = 1000 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 //

    fun setBanner() {

        mViewPagerAdapter = ViewPagerAdapter(thiscontext, listBanner)
        binding.tabViewpager.adapter = mViewPagerAdapter

        val handler = Handler()
        val Update = Runnable {
            if (currentPage === listBanner.size - 1) {
                currentPage = 0
            }
            binding.tabViewpager.setCurrentItem(currentPage++, true)
        }

        timer = Timer() // This will create a new Thread

        timer!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    fun setReview() {

        mReviewAdapter = ReviewAdapter(thiscontext, listReview)
        binding.reviewViewPager.adapter = mReviewAdapter

        val handler = Handler()
        val Update = Runnable {
            if (currentPageTwo === listReview.size - 1) {
                currentPageTwo = 0
            }
            binding.tabViewpager.setCurrentItem(currentPageTwo++, true)
        }

        timerTwo = Timer() // This will create a new Thread

        timerTwo!!.schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                handler.post(Update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

    fun getData() {
        progressDialog!!.show()
        var apiInterface: ApiInterface =
            RetrofitManager().instance!!.create(ApiInterface::class.java)

        apiInterface.dashBoardData.enqueue(object : Callback<DashboardModel> {
            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
                Toast.makeText(thiscontext, " " + t.message.toString(), Toast.LENGTH_LONG)
                    .show()
                progressDialog!!.dismiss()
            }

            override fun onResponse(
                call: Call<DashboardModel>,
                response: Response<DashboardModel>
            ) {
                progressDialog!!.dismiss()
                try {
                    if (response.isSuccessful) {

                        Log.d(TAG, "onResponse: " + response.body().toString())
                        val data: DashboardModel = response.body()!!

                        if (data.status != null && data.status) {
                            listService.addAll(data.services)
                            listDoctor.addAll(data.doctors)
                            listReview.addAll(data.reviews)
                            listBanner.add(data.banner.homepage)
                            listBanner.add(data.banner.aboutpage)
                            listBanner.add(data.banner.servicedetail)
                            listBanner.add(data.banner.doctorpage)
                            listBanner.add(data.banner.contactpage)
                            listBanner.add(data.banner.contactpagemain)
                            listBanner.add(data.banner.loginpage)

                            adapterDoctor?.notifyDataSetChanged()
                            adapterService?.notifyDataSetChanged()
                            mViewPagerAdapter?.notifyDataSetChanged()
                            mReviewAdapter?.notifyDataSetChanged()

                        } else {
                            Utility.showDialog(
                                thiscontext,
                                SweetAlertDialog.ERROR_TYPE, data.message
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