package com.clinicscluster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clinicscluster.R
import com.clinicscluster.databinding.AdapterHomeDetailsBinding
import com.clinicscluster.databinding.DashboardDoctorLayoutBinding
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.AboutUsListHomeModel
import com.clinicscluster.model.dashboard.Doctor

class AboutDashBoardListAdapter(private var mOptionList: List<AboutUsListHomeModel>, var context: Context) :
    RecyclerView.Adapter<AboutDashBoardListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewProductCategoryBinding: AdapterHomeDetailsBinding =
            AdapterHomeDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewProductCategoryBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)

//        if (option.profileImage != null) {
//            Utility.setImage(context, option.profileImage, holder.binding.image)
//        }


        if (option._key.equals("site_contact_number" ,true)) {
            holder.binding.txtKey.text = "Call Now For Free"
            holder.binding.txtValue.text = option._value
            holder.binding.image.setImageResource(R.drawable.phone)
        }
        if (option._key.equals("site_address" ,true)) {
            holder.binding.txtKey.text = "Address Here"
            holder.binding.txtValue.text = option._value
            holder.binding.image.setImageResource(R.drawable.location)
        }
        if (option._key.equals("site_clock" ,true)) {
            holder.binding.txtKey.text = "Opening Hours"
            holder.binding.txtValue.text = "Mon-Sat: 8:00 am \n" +
                    "8:00 pm"
            holder.binding.image.setImageResource(R.drawable.clock)
        }





    }

    fun updateList(list: ArrayList<AboutUsListHomeModel>) {
        mOptionList = list
        notifyDataSetChanged()
    }


    private fun getItem(index: Int): AboutUsListHomeModel {
        return mOptionList[index]
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }


    inner class ViewHolder(val binding: AdapterHomeDetailsBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
            ////
        }
    }
}