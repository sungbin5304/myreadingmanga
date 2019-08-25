package com.sungbin.com.myreadingmanga.adapter

import android.app.Activity
import android.content.Intent
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sungbin.com.myreadingmanga.R
import com.sungbin.com.myreadingmanga.activity.MangaPageActivity
import com.sungbin.com.myreadingmanga.dto.MangaListItem


class MangaListAdapter(private val list: ArrayList<MangaListItem>?,
                       private val act: Activity) :
    RecyclerView.Adapter<MangaListAdapter.MangaViewHolder>() {

    inner class MangaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MangaViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.manga_list_view, viewGroup, false)
        return MangaViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewholder: MangaViewHolder, position: Int) {
        viewholder.title.text = list!![position].name
        Glide.with(act).load(list[position].thumbnail).format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(viewholder.thumbnail)
        viewholder.thumbnail.setOnClickListener {
            act.startActivity(Intent(act, MangaPageActivity::class.java)
                .putExtra("name", list[position].name)
                .putExtra("link", list[position].link))
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    fun getItem(position: Int): MangaListItem {
        return list!![position]
    }

}
