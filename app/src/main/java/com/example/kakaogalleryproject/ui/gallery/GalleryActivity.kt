package com.example.kakaogalleryproject.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kakaogalleryproject.R
import com.example.kakaogalleryproject.ui.splash.SplashActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class GalleryActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var viewModel: GalleryViewModel
    private var downloadTask: Disposable? = null
    private val adapter = GalleryAdapter()
    private var llManager: RecyclerView.LayoutManager? = null
    private var glManager: RecyclerView.LayoutManager? = null

    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        setContentView(R.layout.activity_main)

        initializeUI()
        downloadTask()
    }

    private fun initializeUI() {
        recycler_view.setHasFixedSize(true)

        // viewmodel
        viewModel = ViewModelProvider(this).get(GalleryViewModel::class.java)
        viewModel.getImgs().observe(this, Observer { imgs -> adapter.setImgs(imgs)})

        // set recyclerview
        glManager = GridLayoutManager(this, 3)
        llManager = LinearLayoutManager(this)
        recycler_view.layoutManager = llManager
        recycler_view.adapter = adapter
    }

    private fun sortImgList(method: Int) = viewModel.sortBy(method)

    private fun downloadTask() {
        // onPreExecute
        progress_bar!!.visibility = View.VISIBLE
        downloadTask = Observable.fromCallable {
            viewModel.downloadTask() // onBackground
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    // onPostExecute
                    adapter.notifyDataSetChanged() // dataset changed
                    progress_bar.visibility = View.GONE
                    downloadTask!!.dispose()

                    // set onClickListener after showing all images
                    sort_index_btn.setOnClickListener {
                        sortImgList(1)
                        sort_index_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                        sort_atoz_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sort_date_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    }
                    sort_atoz_btn.setOnClickListener {
                        sortImgList(2)
                        sort_index_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sort_atoz_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                        sort_date_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    }
                    sort_date_btn.setOnClickListener {
                        sortImgList(3)
                        sort_index_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sort_atoz_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        sort_date_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))
                    }
                    show_list_btn.setOnClickListener {
                        recycler_view.layoutManager = llManager
                        show_list_btn.alpha = 1.0f
                        show_grid_btn.alpha = 0.2f
                    }
                    show_grid_btn.setOnClickListener {
                        recycler_view.layoutManager = glManager
                        show_list_btn.alpha = 0.2f
                        show_grid_btn.alpha = 1.0f
                    }
                }
    }

}