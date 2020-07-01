package com.example.kakaogalleryproject.data

data class ImgInfo(val orgIdx: Int,
                   val href: String,
                   val date: String,
                   val name: String) {
    override fun toString(): String{
        return "$orgIdx, $href, $date, $name"
    }
}