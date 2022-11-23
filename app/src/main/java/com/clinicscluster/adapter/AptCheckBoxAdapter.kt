package com.clinicscluster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.clinicscluster.databinding.AdapterCheckboxBinding
import com.clinicscluster.databinding.AppointmentAdapterBinding
import com.clinicscluster.helper.ApplicationInit
import com.clinicscluster.model.ServiceModel
import com.clinicscluster.model.appointment.Appointment

class AptCheckBoxAdapter(
    private var mOptionList: ArrayList<ServiceModel>,
    var context: Context
) :
    RecyclerView.Adapter<AptCheckBoxAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewProductCategoryBinding: AdapterCheckboxBinding =
            AdapterCheckboxBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewProductCategoryBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)

        try {

            holder.binding.checkbox.setText(option.name)

            holder.binding.checkbox.setOnCheckedChangeListener { compoundButton, b ->
                option.isChecked = b
            }

        } catch (e: Exception) {
            Toast.makeText(context, " " + e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun updateList(list: ArrayList<ServiceModel>) {
        mOptionList = list
        notifyDataSetChanged()
    }


    private fun getItem(index: Int): ServiceModel {
        return mOptionList[index]
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }


    inner class ViewHolder(val binding: AdapterCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
            ////
        }
    }
}