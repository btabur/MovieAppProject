package com.btabur.movieapp2


import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.annotation.SuppressLint
import android.content.Context
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class Ataptor_Same(private val mcontext: Context, private val mData: List<MovieModel>) :
    RecyclerView.Adapter<Ataptor_Same.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View
        val inflater = LayoutInflater.from(mcontext)
        v = inflater.inflate(R.layout.item_same, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.title_same.text = mData[position].title + " (" + mData[position].date!!.substring(0, 4) + ")"
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
        var title_same: TextView
        var img: ImageView
        var item_row: ConstraintLayout

        init {
            title_same = itemView.findViewById(R.id.text_title_same)
            img = itemView.findViewById(R.id.img_same)
            item_row = itemView.findViewById(R.id.item_same)
        }
    }
}