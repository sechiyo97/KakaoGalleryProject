package com.example.kakaogalleryproject.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kakaogalleryproject.data.ImgRepository

class ImgViewModel(application: Application) : AndroidViewModel(application) {

    enum class SORTER { SORT_BY_INDEX, SORT_BY_NAME, SORT_BY_DATE }
    enum class LAYOUT { LINEAR_LAYOUT, GRID_LAYOUT }

    private val imgRepository = ImgRepository()

    fun getImgs() = imgRepository.getImgs()

    fun sortBy(method: SORTER) = imgRepository.sortBy(method.ordinal)

    fun downloadImgs() = imgRepository.downloadImgs()
}