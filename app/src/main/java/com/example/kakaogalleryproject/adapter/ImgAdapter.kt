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
import com.example.kakaogalleryproject.ui.main.dialog.ImgDialog
import com.example.kakaogalleryproject.R
import com.example.kakaogalleryproject.data.Img
import java.util.ArrayList

class ImgAdapter : RecyclerView.Adapter<ImgAdapter.CustomViewHolder>() {
    private var imgList: List<Img> = ArrayList()
    private var context: Context? = null

    inner class CustomViewHolder constructor(view: View) : ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.recyclerview_image)
        var adapterProgressBar: ProgressBar = view.findViewById(R.id.adapter_progress_bar)

        init { context = view.context }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.single_img, viewGroup, false) // inflate viewholder
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int): Unit {
        viewholder.adapterProgressBar.visibility = View.VISIBLE // show progress bar while loading a image

        // glide image in
        Glide.with(context!!)
                .load(imgList[position].src) // load url
                .transition(DrawableTransitionOptions.withCrossFade()) // fade in
                .centerCrop() // fit center
                .listener(object : RequestListener<Drawable?> { // after load, remove progress bar
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

        // show image dialog when clicked
        viewholder.imageView.setOnClickListener {
            val imgDialog = ImgDialog(context!!)
            imgDialog.setInfo(imgList[position].name, imgList[position].src)
            imgDialog.show()
        }
    }

    override fun getItemCount(): Int = imgList.size

    fun setImgs(imgs: List<Img>) {
        this.imgList = imgs
        notifyDataSetChanged() // apply to UI
    }
}