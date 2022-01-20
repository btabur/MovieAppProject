package com.btabur.movieapp2


import com.btabur.movieapp2.MovieModel
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.btabur.movieapp2.R
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import android.widget.ImageView

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class Adaptor_Recycler(private val mcontext: Context, private val mData: List<MovieModel>) :
    RecyclerView.Adapter<Adaptor_Recycler.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View
        val inflater = LayoutInflater.from(mcontext)
        v = inflater.inflate(R.layout.movie_items, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.title.text = mData[position].title + " (" + mData[position].date!!.substring(0, 4) + ")"

        holder.description.text = mData[position].description!!.substring(0, 50) + "..."
        holder.date.text = mData[position].date
        Glide.with(mcontext).load("https://image.tmdb.org/t/p/w500" + mData[position].img)
            .into(holder.img)


        holder.item_row.setOnClickListener {
            val intent = Intent(mcontext, MovieDetay::class.java)
            intent.putExtra("id", mData[position].id)
            intent.putExtra("image", mData[position].img)
            intent.putExtra("title", mData[position].title)
            intent.putExtra("description", mData[position].description)
            intent.putExtra("date", mData[position].date)
            intent.putExtra("vote_avarage", mData[position].vote_avrage)
            mcontext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var description: TextView
        var date: TextView
        var img: ImageView
        var item_row: ConstraintLayout

        init {
            title = itemView.findViewById(R.id.text_title)
            description = itemView.findViewById(R.id.text_description)
            date = itemView.findViewById(R.id.text_date)
            img = itemView.findViewById(R.id.imageView)
            item_row = itemView.findViewById(R.id.item_row)
        }
    }
}