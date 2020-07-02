package com.example.kakaogalleryproject.ui.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.kakaogalleryproject.data.Img
import com.example.kakaogalleryproject.data.ImgRepository

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    private val imgRepository = ImgRepository()

    fun getImgs() = imgRepository.getImgs();
    fun addImg(img: Img) = imgRepository.addImg(img)
    fun sortBy(method: Int) = imgRepository.sortBy(method)
    fun downloadTask() = imgRepository.downloadTask()
}