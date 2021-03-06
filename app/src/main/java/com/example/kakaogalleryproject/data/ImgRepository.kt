package com.example.kakaogalleryproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kakaogalleryproject.constants.BASE_URL
import org.jsoup.Jsoup

class ImgRepository {

    inner class IndexComparator : Comparator<Img> {
        override fun compare(a: Img, b: Img): Int =  a.orgIdx - b.orgIdx
    }

    inner class NameComparator : Comparator<Img> {
        override fun compare(a: Img, b: Img): Int =  a.name.compareTo(b.name)
    }

    inner class DateComparator : Comparator<Img> {
        override fun compare(a: Img, b: Img): Int {
            val dateComp = b.date.compareTo(a.date)
            val nameComp = a.name.compareTo(b.name)
            return if (dateComp != 0) dateComp else nameComp
        }
    }

    private val imgList = mutableListOf<Img>()
    private val imgs = MutableLiveData<List<Img>>()

    init { imgs.value = imgList } // change livedata

    private fun addImg(img: Img) = imgList.add(img)

    fun getImgs() = imgs as LiveData<List<Img>> // return livedata

    fun sortBy(method: Int) {
        when (method) {
            0 -> imgList.sortWith(IndexComparator()) // sort by index
            1 -> imgList.sortWith(NameComparator()) // sort by name
            2 -> imgList.sortWith(DateComparator())
        }
        imgs.value = imgList // change livedata
    }
    
    fun downloadImgs(){
        imgList.clear() // download new data
        try{
            val doc = Jsoup.connect(BASE_URL).timeout(10000).get() // get html
            val imageSelector = ".jq-lazy" // selector
            val imgElements = doc.select(imageSelector) // get images
            for (i in imgElements.indices) { // find and insert data
                val imgElement = imgElements[i]
                val src = imgElement.attributes()["data-src"]
                val date = src.substring(54..61)

                val alt = imgElement.attributes()["alt"]
                val name = alt.substring(0..alt.length-33)

                val img = Img(i, src, date, name)
                addImg(img)
            }
        } catch(e: Exception) {e.printStackTrace();}
        imgs.postValue(imgList) // background invokation of changing livedata
    }
}