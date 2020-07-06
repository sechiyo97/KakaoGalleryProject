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
import kotlinx.android.synthetic.main.single_img.view.*
import java.util.ArrayList

class ImgAdapter : RecyclerView.Adapter<ImgAdapter.CustomViewHolder>() {
    private var imgList: List<Img> = ArrayList()
    private var context: Context? = null

    inner class CustomViewHolder(val view: View) : ViewHolder(view)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CustomViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.single_img, viewGroup, false) // inflate viewholder
        return CustomViewHolder(view)
    }

    // 추후 코드 확인하며 수정하였음 (CustomViewholder Contructor changed)
    override fun onBindViewHolder(viewholder: CustomViewHolder, position: Int): Unit {
        viewholder.view.adapter_progress_bar.visibility = View.VISIBLE // show progress bar while loading a image

        // glide image in
        Glide.with(context!!)
                .load(imgList[position].src) // load url
                .transition(DrawableTransitionOptions.withCrossFade()) // fade in
                .centerCrop() // fit center
                .listener(object : RequestListener<Drawable?> { // after load, remove progress bar
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        viewholder.view.adapter_progress_bar.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        viewholder.view.adapter_progress_bar.visibility = View.GONE
                        return false
                    }
                })
                .into(viewholder.view.recyclerview_image)

        // show image dialog when clicked
        viewholder.view.recyclerview_image.setOnClickListener {
            val imgDialog = ImgDialog(context!!)
            imgDialog.start(imgList[position].name, imgList[position].src)
        }
    }

    override fun getItemCount(): Int = imgList.size

    fun setImgs(imgs: List<Img>) {
        this.imgList = imgs
        notifyDataSetChanged() // apply to UI
    }
}