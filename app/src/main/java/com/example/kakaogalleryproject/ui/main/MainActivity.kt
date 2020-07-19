package com.example.kakaogalleryproject.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kakaogalleryproject.R
import com.example.kakaogalleryproject.constants.*
import com.example.kakaogalleryproject.adapter.ImgAdapter
import com.example.kakaogalleryproject.ui.splash.SplashActivity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ImgViewModel // nonnull
    private var curLayout = ImgViewModel.LAYOUT.LINEAR_LAYOUT // linear for 0

    private val adapter = ImgAdapter() // initialize at once
    private var llManager = LinearLayoutManager(this) // default linearlayout
    private var glManager = GridLayoutManager(this, PORTRAIT_GRID_SPAN) // default gridlayout

    private var downloadTask: Disposable? = null // could be executed more than twice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        setContentView(R.layout.activity_main)
        initUI() // initialize UI
    }

    private fun showSplash(){
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        setTheme(R.style.AppTheme)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateLayout()
    }

    private fun initUI() {
        // set index order button
        sort_index_btn.isSelected = true

        // set recyclerview
        recycler_view.setHasFixedSize(true)
        updateLayout()
        recycler_view.adapter = adapter

        // viewmodel
        viewModel = ViewModelProvider(this).get(ImgViewModel::class.java) // get a viewmodel
        viewModel.getImgs().observe(this, Observer { imgs -> adapter.setImgs(imgs)}) // change adapter imags when changed

        // init sort & layout buttons
        initButtons()

        // download images
        downloadImgs()
    }

    private fun downloadImgs() {
        // onPreExecute
        progress_bar.visibility = View.VISIBLE // progress bar appear
        downloadTask = Completable.fromCallable {
            viewModel.downloadImgs() // onBackground
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    // onPostExecute
                    progress_bar.visibility = View.GONE // progress bar disappear
                    downloadTask!!.dispose()
                }
    }

    private fun initButtons(){// set onClickListener after showing all images
        goup_btn.setOnClickListener { recycler_view.smoothScrollToPosition(0); } // scroll to top button

        // sorting buttons
        sort_index_btn.setOnClickListener {
            sortImgList(ImgViewModel.SORTER.SORT_BY_INDEX)
            sort_index_btn.isSelected = true
            sort_atoz_btn.isSelected = false
            sort_date_btn.isSelected = false
        }
        sort_atoz_btn.setOnClickListener {
            sortImgList(ImgViewModel.SORTER.SORT_BY_NAME)
            sort_index_btn.isSelected = false
            sort_atoz_btn.isSelected = true
            sort_date_btn.isSelected = false
        }
        sort_date_btn.setOnClickListener {
            sortImgList(ImgViewModel.SORTER.SORT_BY_DATE)
            sort_index_btn.isSelected = false
            sort_atoz_btn.isSelected = false
            sort_date_btn.isSelected = true
        }

        // layout type selection buttons
        show_list_btn.setOnClickListener {
            curLayout = ImgViewModel.LAYOUT.LINEAR_LAYOUT
            updateLayout()
            show_list_btn.alpha = 1.0f
            show_grid_btn.alpha = 0.2f
        }
        show_grid_btn.setOnClickListener {
            curLayout = ImgViewModel.LAYOUT.GRID_LAYOUT
            updateLayout()
            show_list_btn.alpha = 0.2f
            show_grid_btn.alpha = 1.0f
        }
    }

    private fun updateLayout() {
        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) { // landscape
            glManager.spanCount = LANDSCAPE_GRID_SPAN // span 6 for wide screen
            title_top.layoutParams.height = (resources.displayMetrics.density * 40).toInt() // shorter title
            layout_selector.visibility = View.INVISIBLE // hide layout selector
            recycler_view.layoutManager = glManager // grid layout
        } else { // portrait
            glManager.spanCount = PORTRAIT_GRID_SPAN // span 3 for narrow screen
            title_top.layoutParams.height = (resources.displayMetrics.density * 70).toInt() // longer title
            layout_selector.visibility = View.VISIBLE // show layout selector
            when (curLayout) { // layout selection
                ImgViewModel.LAYOUT.LINEAR_LAYOUT -> recycler_view.layoutManager = llManager
                else -> recycler_view.layoutManager = glManager
            }
        }
        recycler_view.smoothScrollToPosition(0); // to top
    }
    private fun sortImgList(method: ImgViewModel.SORTER) = viewModel.sortBy(method)
}