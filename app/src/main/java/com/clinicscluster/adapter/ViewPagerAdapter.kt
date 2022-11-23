package com.clinicscluster.adapter

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.ImageView
import com.clinicscluster.R
import com.clinicscluster.helper.Utility
import java.util.*
import java.util.logging.Handler
import kotlin.collections.ArrayList

class ViewPagerAdapter(// Context object
    var context: Context, // Array of images
    var images: ArrayList<String>
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
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView = mLayoutInflater.inflate(R.layout.dashboard_slider, container, false)

        // referencing the image view from the item.xml file
        val imageView = itemView.findViewById<View>(R.id.imageViewMain) as ImageView

        // setting the image in the imageView
//        imageView.setImageResource(images[position])
        if (images[position] != null) {
            Utility.setImage(context, images[position].toString(), imageView)
        }
        // Adding the View
        Objects.requireNonNull(container).addView(itemView)




        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}