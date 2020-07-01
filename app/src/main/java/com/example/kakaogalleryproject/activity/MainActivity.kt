package com.example.kakaogalleryproject.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaogalleryproject.R
import com.example.kakaogalleryproject.model.ImgAdapter
import com.example.kakaogalleryproject.model.ImgInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mProgress: ProgressBar? = null
    private var downloadTask: Disposable? = null
    private var sortIndexBtn: LinearLayout? = null
    private var sortAtoZBtn: LinearLayout? = null
    private var sortDateBtn: LinearLayout? = null
    private var showListBtn: ImageView? = null
    private var showGridBtn: ImageView? = null
    private var imageList = ArrayList<ImgInfo>()
    private var recyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var llManager: RecyclerView.LayoutManager? = null
    private var glManager: RecyclerView.LayoutManager? = null
    private val baseURL = "https://www.gettyimagesgallery.com/collection/sasha"
    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        setContentView(R.layout.activity_main)
        sortIndexBtn = findViewById(R.id.sort_index_btn)
        sortAtoZBtn = findViewById(R.id.sort_atoz_btn)
        sortDateBtn = findViewById(R.id.sort_date_btn)
        showListBtn = findViewById(R.id.show_list_btn)
        showGridBtn = findViewById(R.id.show_grid_btn)
        mProgress = findViewById(R.id.progress_bar)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.setHasFixedSize(true)

        // use a staggered grid layout manager
        glManager = GridLayoutManager(this, 3)
        llManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = llManager

        // adapter
        mAdapter = ImgAdapter(imageList)
        recyclerView?.adapter = mAdapter
        downloadTask()
    }

    private fun downloadTask() {
        // onPreExecute
        mProgress!!.visibility = View.VISIBLE
        downloadTask = Observable.fromCallable {
            // onBackground
            try{
                var doc = Jsoup.connect(baseURL).timeout(10000).get()
                val imageSelector = ".jq-lazy"
                val images = doc.select(imageSelector) // get images
                for (i in images.indices) {
                    val image = images[i]
                    val href = image.attributes()["data-src"]
                    val date = getDateFromHref(href)
                    val name = image.attributes()["alt"]
                    val imgInfo = ImgInfo(i, href, date, name)
                    imageList.add(imgInfo)
                }
            } catch(e: Exception) {e.printStackTrace();}
            false
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    // onPostExecute
                    mAdapter!!.notifyDataSetChanged() // dataset changed
                    mProgress!!.visibility = View.GONE
                    downloadTask!!.dispose()

                    // set onClickListener after showing all images
                    sortIndexBtn!!.setOnClickListener {
                        sortImgList(1)
                        sortIndexBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                        sortAtoZBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sortDateBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    }
                    sortAtoZBtn!!.setOnClickListener {
                        sortImgList(2)
                        sortIndexBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sortAtoZBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                        sortDateBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    }
                    sortDateBtn!!.setOnClickListener {
                        sortImgList(3)
                        sortIndexBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sortAtoZBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sortDateBtn!!.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                    }
                    showListBtn!!.setOnClickListener {
                        recyclerView!!.layoutManager = llManager
                        showListBtn!!.alpha = 1.0f
                        showGridBtn!!.alpha = 0.2f
                    }
                    showGridBtn!!.setOnClickListener {
                        recyclerView!!.layoutManager = glManager
                        showListBtn!!.alpha = 0.2f
                        showGridBtn!!.alpha = 1.0f
                    }
                }
    }

    private fun getDateFromHref(href: String): String {
        return href.substring(54..61)
    }

    private fun sortImgList(method: Int) {
        when (method) {
            1 -> imageList.sortWith(Comparator { a: ImgInfo, b: ImgInfo -> a.orgIdx - b.orgIdx })
            2 -> imageList.sortWith(Comparator { a: ImgInfo, b: ImgInfo -> a.name.compareTo(b.name) })
            3 -> imageList.sortWith(Comparator { a: ImgInfo, b: ImgInfo ->
                val dateComp = b.date.compareTo(a.date)
                val nameComp = a.name.compareTo(b.name)
                if (dateComp != 0) dateComp else nameComp
            })
        }
        mAdapter!!.notifyDataSetChanged()
    }
}