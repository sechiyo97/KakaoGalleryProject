package com.example.kakaogalleryproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.Jsoup

class ImgRepository {

    private val baseURL = "https://www.gettyimagesgallery.com/collection/sasha"

    private val imgList = mutableListOf<Img>()
    private val imgs = MutableLiveData<List<Img>>()

    init { imgs.value = imgList }

    fun addImg(img: Img) = imgList.add(img)
    fun getImgs() = imgs as LiveData<List<Img>>

    fun sortBy(method: Int) {
        when (method) {
            1 -> imgList.sortWith(Comparator { a: Img, b: Img -> a.orgIdx - b.orgIdx })
            2 -> imgList.sortWith(Comparator { a: Img, b: Img -> a.name.compareTo(b.name) })
            3 -> imgList.sortWith(Comparator { a: Img, b: Img ->
                val dateComp = b.date.compareTo(a.date)
                val nameComp = a.name.compareTo(b.name)
                if (dateComp != 0) dateComp else nameComp
            })
        }
        imgs.value = imgList
    }

    fun downloadTask(){
        try{
            val doc = Jsoup.connect(baseURL).timeout(10000).get()
            val imageSelector = ".jq-lazy"
            val imgs = doc.select(imageSelector) // get images
            for (i in imgs.indices) {
                val imgElement = imgs[i]
                val href = imgElement.attributes()["data-src"]
                val date = href.substring(54..61)
                val name = imgElement.attributes()["alt"]
                val img = Img(i, href, date, name)
                addImg(img)
            }
        } catch(e: Exception) {e.printStackTrace();}

    }
}