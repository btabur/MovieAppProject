package com.btabur.movieapp2

import android.content.Context
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.LinearLayout
import android.content.Intent
import android.view.View

class Adaptor_search(context: Context, private val models: List<MovieModel>) :
    ArrayAdapter<MovieModel>(
        context, 0, models
    ) {
    private val inflater: LayoutInflater
    private var holder: ViewHolder? = null
    override fun getCount(): Int {
        return models.size
    }

    override fun getItem(position: Int): MovieModel {
        return models[position]
    }

    override fun getItemId(position: Int): Long {
        return models[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search, null)
            holder = ViewHolder()
            holder!!.text_title = convertView.findViewById(R.id.text_search_item)
            holder!!.layout = convertView.findViewById(R.id.item_search)
            convertView.tag = holder
            holder!!.layout.setOnClickListener(View.OnClickListener {
                val intent = Intent(context, MovieDetay::class.java)
                intent.putExtra("id", models[position].id)
                intent.putExtra("image", models[position].img)
                intent.putExtra("title", models[position].title)
                intent.putExtra("description", models[position].description)
                intent.putExtra("date", models[position].date)
                intent.putExtra("vote_avarage", models[position].vote_avrage)
                context.startActivity(intent)
            })
        } else {

            holder = convertView.tag as ViewHolder
        }
        val model = models[position]
        if (model != null) {
            holder!!.text_title!!.text = model.title
        }
        return convertView!!
    }


    private class ViewHolder {
        var text_title: TextView? = null
        lateinit var layout: LinearLayout
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}