package com.example.kakaogalleryproject.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kakaogalleryproject.R
import java.util.ArrayList

class ImgAdapter constructor(imageList: ArrayList<ImgInfo>?) : RecyclerView.Adapter<ImgAdapter.CustomViewHolder>() {
    private val imageList: ArrayList<ImgInfo>? = imageList
    private var mContext: Context? = null

    inner class CustomViewHolder constructor(view: View) : ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.recyclerview_image)
        var adapterProgressBar: ProgressBar = view.findViewById(R.id.adapter_progress_bar)

        init {
            mContext = view.context
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.single_image, viewGroup, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int): Unit {
        viewholder.adapterProgressBar.visibility = View.VISIBLE
        Glide.with(mContext!!)
                .load(imageList?.get(position)?.href)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        viewholder.adapterProgressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        viewholder.adapterProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(viewholder.imageView)
    }

    override fun getItemCount(): Int {
        return (imageList?.size ?: 0)
    }


}