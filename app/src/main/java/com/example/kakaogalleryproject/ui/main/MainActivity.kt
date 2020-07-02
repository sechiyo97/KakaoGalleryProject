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
    private lateinit var viewModel: ImgViewModel // must be nonnull

    private val adapter = ImgAdapter()
    private var llManager: RecyclerView.LayoutManager? = null
    private var glManager: RecyclerView.LayoutManager? = null

    private var downloadTask: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showSplash() // show splash screen for 2s while loading
        setTheme(R.style.AppTheme)

        initUI() // initialize UI
    }

    // show splash activity for 2s while loading
    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private fun initUI() {
        // viewmodel
        viewModel = ViewModelProvider(this).get(ImgViewModel::class.java)
        viewModel.getImgs().observe(this, Observer { imgs -> adapter.setImgs(imgs)})

        // set recyclerview
        recycler_view.setHasFixedSize(true)
        glManager = GridLayoutManager(this, 3)
        llManager = LinearLayoutManager(this)
        recycler_view.layoutManager = llManager
        recycler_view.adapter = adapter

        downloadTask()
    }

    private fun sortImgList(method: Int) = viewModel.sortBy(method)

    private fun downloadTask() {
        // onPreExecute
        progress_bar.visibility = View.VISIBLE
        downloadTask = Observable.fromCallable {
            viewModel.downloadImgs() // onBackground
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // onPostExecute
                    progress_bar.visibility = View.GONE
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

}