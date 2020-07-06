package com.example.kakaogalleryproject.ui.main.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
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
class ImgDialog(context: Context) : Dialog(context){

    private lateinit var title : String
    private lateinit var src : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // settings
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.img_dialog)
    }
    fun setInfo(title: String, src: String){
        this.title = title
        this.src = src
    }
    override fun show() {
        super.show()
        // widgets
        val theDialog = findViewById<View>(R.id.img_dialog) // full dialog
        val imgInfo = findViewById<View>(R.id.img_info) as TextView
        val imgView = findViewById<View>(R.id.img_view) as ImageView
        val dialogProgressBar = findViewById<View>(R.id.dialog_progress_bar) as ProgressBar

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

        theDialog.setOnClickListener(){ dismiss() }
    }

}
