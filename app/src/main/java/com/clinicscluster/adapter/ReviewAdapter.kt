package com.clinicscluster.adapter

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.clinicscluster.R
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.Review
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList

class ReviewAdapter(// Context object
    var context: Context, // Array of images
    var list: ArrayList<Review>
) : PagerAdapter() {
    // Layout Inflater
    var mLayoutInflater: LayoutInflater

    // Viewpager Constructor
    init {
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        // return the number of images
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView = mLayoutInflater.inflate(R.layout.dashboard_review, container, false)

        // referencing the image view from the item.xml file
        val imageProfile = itemView.findViewById<View>(R.id.imageProfile) as ImageView
        val txtName = itemView.findViewById(R.id.txtName) as TextView
        val txtPosition = itemView.findViewById(R.id.txtPosition) as TextView
        val txtReview = itemView.findViewById(R.id.txtReview) as TextView



        // setting the image in the imageView
//        imageView.setImageResource(images[position])
        if (list[position].image != null) {
            Utility.setImage(context, list[position].image, imageProfile)
        }

        txtName.setText(list.get(position).name)
        txtPosition.setText(list.get(position).position)
        txtReview.setText(list.get(position).description)
        // Adding the View
        Objects.requireNonNull(container).addView(itemView)




        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}