package com.btabur.movieapp2

import android.content.Context

import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import android.widget.ImageView

class Adaptor_Slider(private val mcontext: Context, private val movieModelList: List<MovieModel>?) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_slider, container, false)
        val imgPhoto = view.findViewById<ImageView>(R.id.image_foto)
        val text_title = view.findViewById<TextView>(R.id.text_title)
        val text_description = view.findViewById<TextView>(R.id.text_description)
        val model = movieModelList!![position]

            Glide.with(mcontext).load("https://image.tmdb.org/t/p/w500" + model.img).into(imgPhoto)

            text_title.text = model.title + " (" + model.date!!.substring(0, 4) + ")"
            text_description.text = model.description!!.substring(0, model.description!!.length / 2) + "..."

        imgPhoto.setOnClickListener {
            val intent = Intent(mcontext, MovieDetay::class.java)
            intent.putExtra("id", movieModelList[position].id)
            intent.putExtra("image", movieModelList[position].img)
            intent.putExtra("title", movieModelList[position].title)
            intent.putExtra("description", movieModelList[position].description)
            intent.putExtra("date", movieModelList[position].date)
            intent.putExtra("vote_avarage", movieModelList[position].vote_avrage)
            mcontext.startActivity(intent)
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return movieModelList?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}