package com.sungbin.com.myreadingmanga.adapter

import android.app.Activity
import android.content.Intent
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sungbin.com.myreadingmanga.R
import com.sungbin.com.myreadingmanga.activity.MangaPageActivity
import com.sungbin.com.myreadingmanga.dto.MangaFavItem

class MangaFavAdapter(private val list: ArrayList<MangaFavItem>?,
                       private val act: Activity) :
    RecyclerView.Adapter<MangaFavAdapter.FavViewHolder>() {

    inner class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.manga_fav_list, viewGroup, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewholder: FavViewHolder, position: Int) {
        viewholder.title.text = list!![position].name
        viewholder.title.setOnClickListener {
            act.startActivity(
                Intent(act, MangaPageActivity::class.java)
                    .putExtra("name", list[position].name)
                    .putExtra("link", list[position].link))
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun getItem(position: Int): MangaFavItem {
        return list!![position]
    }

}
