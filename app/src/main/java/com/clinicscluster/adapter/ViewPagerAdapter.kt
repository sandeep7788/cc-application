package com.clinicscluster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.clinicscluster.R
import com.clinicscluster.helper.Utility
import com.clinicscluster.model.dashboard.Slider
import java.util.*
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream


class ViewPagerAdapter(// Context object
    var context: Context, // Array of images
    var images: ArrayList<Slider>
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
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // inflating the item.xml
        val itemView = mLayoutInflater.inflate(R.layout.dashboard_slider, container, false)

        // referencing the image view from the item.xml file
        val imageView = itemView.findViewById<View>(R.id.imageViewMain) as ImageView
        val title = itemView.findViewById<View>(R.id.txtTitle) as TextView
        val des = itemView.findViewById<View>(R.id.txtShortDes) as TextView
        val txtLongDes = itemView.findViewById(R.id.txtLongDesTwo) as TextView

        // setting the image in the imageView
//        imageView.setImageResource(images[position])
        if (images[position] != null) {
            Utility.setImage(context, images[position].image.toString(), imageView)

//            try {
//                val inputStream: InputStream = context.assets.open(images[position].image.toString())
//                val bufferedInputStream = BufferedInputStream(inputStream)
//                val bmp = BitmapFactory.decodeStream(bufferedInputStream)
//                imageView.setImageBitmap(bmp)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
        }

        title.text = Utility.html2text(images[position].title.toString())
        des.text = Utility.html2text(images[position].heading.toString())
        txtLongDes.text = Utility.html2text(images[position].subheading.toString())
        // Adding the View
        Objects.requireNonNull(container).addView(itemView)




        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}