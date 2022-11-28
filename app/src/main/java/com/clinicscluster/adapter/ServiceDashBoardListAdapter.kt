package com.clinicscluster.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.clinicscluster.activity.ServiceActivity
import com.clinicscluster.databinding.DashboardServiceLayoutBinding
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.Service

class ServiceDashBoardListAdapter(private var mOptionList: List<Service>, var context: Context) : RecyclerView.Adapter<ServiceDashBoardListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewProductCategoryBinding: DashboardServiceLayoutBinding =
            DashboardServiceLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(viewProductCategoryBinding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)

        if (option.image != null) {
            Utility.setImage(context,option.image,holder.binding.image)
        }

        holder.binding.txtName.setText(Utility.html2text(option.title))

        holder.binding.item.setOnClickListener {
            var intent = Intent(context, ServiceActivity::class.java)
            intent.putExtra("id", option.id)
            context.startActivity(intent)
        }
    }

    fun updateList(list: ArrayList<Service>) {
        mOptionList = list
        notifyDataSetChanged()
    }


    private fun getItem(index: Int): Service {
        return mOptionList[index]
    }

    override fun getItemCount(): Int {
        return mOptionList.size
    }


    inner class ViewHolder(val binding: DashboardServiceLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        override fun onClick(v: View?) {

        }
    }

}