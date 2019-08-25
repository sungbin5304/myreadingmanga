package com.sungbin.com.myreadingmanga.adapter

import android.app.Activity
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sungbin.com.myreadingmanga.R

class MangaPageAdapter(private val list: ArrayList<String>?,
                       private val act: Activity) :
    RecyclerView.Adapter<MangaPageAdapter.PageViewHolder>() {

    inner class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.manga_page_view, viewGroup, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewholder: PageViewHolder, position: Int) {
        Glide.with(act).load(list!![position]).format(DecodeFormat.PREFER_ARGB_8888)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(viewholder.image)
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

    fun getItem(position: Int): String {
        return list!![position]
    }

}
