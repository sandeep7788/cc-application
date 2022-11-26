package com.clinicscluster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.clinicscluster.databinding.AppointmentAdapterBinding
import com.clinicscluster.helper.ApplicationInit
import com.clinicscluster.model.appointment.Appointment

class AppointmentListAdapter(
    private var mOptionList: ArrayList<Appointment>,
    var context: Context
) :
    RecyclerView.Adapter<AppointmentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewProductCategoryBinding: AppointmentAdapterBinding =
            AppointmentAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewProductCategoryBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)

        try {

            holder.binding.txtTitle.text = ApplicationInit.readUser()?.name
            holder.binding.txtDes.text = option.description
            holder.binding.txtDate.text = option.appointmentDate
            holder.binding.txtDate.text = option.appointmentDate
            holder.binding.txtDoctor.text = option.doctor.firstName + " " + option.doctor.lastName

            var serviceData = ""
            option.services.forEach {
                serviceData = serviceData + it.name + ", "
            }

            if (serviceData.isNotEmpty())
                serviceData = removeLastChar(serviceData).toString()

            holder.binding.txtService.text = serviceData

            if (option.status == 0) {
                holder.binding.txtStatus.text = "Booked/Upcoming"
            } else if (option.status == 1) {
                holder.binding.txtStatus.text = "Completed"
            } else if (option.status == 2) {
                holder.binding.txtStatus.text = "Cancelled"
            } else if (option.status == 3) {
                holder.binding.txtStatus.text = "Check Out"
            } else if (option.status == 4) {
                holder.binding.txtStatus.text = "Check In"
            } else {
                holder.binding.txtStatus.text = "UnKnown"
            }

        } catch (e: Exception) {
            Toast.makeText(context, " " + e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun removeLastChar(str: String): String? {
        return removeLastChars(str, 2)
    }

    fun removeLastChars(str: String, chars: Int): String? {
        return str.substring(0, str.length - chars)
    }

    fun updateList(list: ArrayList<Appointment>) {
        mOptionList = list
        notifyDataSetChanged()
    }


    private fun getItem(index: Int): Appointment {
        return mOptionList[index]
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }


    inner class ViewHolder(val binding: AppointmentAdapterBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
            ////
        }
    }
}