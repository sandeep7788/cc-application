package com.clinicscluster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clinicscluster.databinding.DashboardDoctorLayoutBinding
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.Doctor

class DoctorDashBoardListAdapter(private var mOptionList: List<Doctor>, var context: Context) :
    RecyclerView.Adapter<DoctorDashBoardListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewProductCategoryBinding: DashboardDoctorLayoutBinding =
            DashboardDoctorLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewProductCategoryBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)

        if (option.profileImage != null) {
            Utility.setImage(context, option.profileImage, holder.binding.image)
        }
        holder.binding.txtDoctorName.text = Utility.html2text(option.firstName)
        holder.binding.txtType.text = Utility.html2text(option.specialties)

    }

    fun updateList(list: ArrayList<Doctor>) {
        mOptionList = list
        notifyDataSetChanged()
    }


    private fun getItem(index: Int): Doctor {
        return mOptionList[index]
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }


    inner class ViewHolder(val binding: DashboardDoctorLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
            ////
        }
    }
}