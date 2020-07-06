package com.example.kakaogalleryproject.ui.main.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.Window
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kakaogalleryproject.R

// custom dialog for image showing
class ImgDialog(private val context: Context) {

    fun start(title:String, src:String) {
        val dlg = Dialog(context)

        // settings
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setContentView(R.layout.img_dialog)

        dlg.show() // show dialog

        // widgets
        val theDialog = dlg.findViewById<View>(R.id.img_dialog) // full dialog
        val imgInfo = dlg.findViewById<View>(R.id.img_info) as TextView
        val imgView = dlg.findViewById<View>(R.id.img_view) as ImageView
        val dialogProgressBar = dlg.findViewById<View>(R.id.dialog_progress_bar) as ProgressBar

        // edit inside dialog
        imgInfo.text = title
        Glide.with(context)
                .load(src) // load url
                .transition(DrawableTransitionOptions.withCrossFade()) // fade in
                .listener(object : RequestListener<Drawable?> { // after load, remove progress bar
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        dialogProgressBar.visibility = View.GONE
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        dialogProgressBar.visibility = View.GONE
                        return false
                    }
                })
                .into(imgView)

        theDialog.setOnClickListener(){ dlg.dismiss() }
    }

}
