package com.example.kakaogalleryproject.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kakaogalleryproject.data.ImgRepository

class ImgViewModel(application: Application) : AndroidViewModel(application) {

    private val imgRepository = ImgRepository()

    fun getImgs() = imgRepository.getImgs();

    fun sortBy(method: Int) = imgRepository.sortBy(method)
    fun downloadImgs() = imgRepository.downloadImgs()
}