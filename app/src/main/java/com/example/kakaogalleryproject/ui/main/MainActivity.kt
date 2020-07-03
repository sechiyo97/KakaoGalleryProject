package com.example.kakaogalleryproject.ui.main

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

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ImgViewModel // nonnull

    private val adapter = ImgAdapter() // initialize at once
    private val llManager = LinearLayoutManager(this)
    private val glManager = GridLayoutManager(this, 3)

    private var downloadTask: Disposable? = null // could be executed more than twice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTheme(R.style.AppTheme) // remove SplashTheme
        initUI() // initialize UI
    }

    private fun initUI() {
        // set recyclerview
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = llManager
        recycler_view.adapter = adapter

        // viewmodel
        viewModel = ViewModelProvider(this).get(ImgViewModel::class.java) // get a viewmodel
        viewModel.getImgs().observe(this, Observer { imgs -> adapter.setImgs(imgs)}) // change adapter imags when changed

        // download images
        downloadImgs()
    }

    private fun downloadImgs() {
        // onPreExecute
        progress_bar.visibility = View.VISIBLE // progress bar appear
        downloadTask = Observable.fromCallable {
            viewModel.downloadImgs() // onBackground
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // onPostExecute
                    progress_bar.visibility = View.GONE // progress bar disappear
                    initButtons() // init sort & layout buttons after loading images
                    downloadTask!!.dispose()
                }
    }

    private fun initButtons(){// set onClickListener after showing all images
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

    private fun sortImgList(method: Int) = viewModel.sortBy(method)
}