package com.clinicscluster.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.clinicscluster.databinding.ServiceListAdapterBinding
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.ServiceListModel

class ServiceListFragmentAdapter(private var mOptionList: java.util.ArrayList<ServiceListModel>, var context: Context) :
    RecyclerView.Adapter<ServiceListFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewProductCategoryBinding: ServiceListAdapterBinding =
            ServiceListAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewProductCategoryBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)

        if (option.image != null) {
            Utility.setImage(context, option.image, holder.binding.image)
        }
        holder.binding.txtTitle.text = option.title
        holder.binding.txtDes.text = option.shortDescription

        holder.binding.item.setOnClickListener {
            var intent = Intent(context, ServiceActivity::class.java)
            intent.putExtra("id", option.id)
            context.startActivity(intent)
        }
    }

    fun updateList(list: ArrayList<ServiceListModel>) {
        mOptionList = list
        notifyDataSetChanged()
    }


    private fun getItem(index: Int): ServiceListModel {
        return mOptionList[index]
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }


    inner class ViewHolder(val binding: ServiceListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {
        }
    }
}