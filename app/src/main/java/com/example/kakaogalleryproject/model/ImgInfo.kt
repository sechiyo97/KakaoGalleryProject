package com.example.kakaogalleryproject.model

class ImgInfo(val orgIdx: Int, href: String, date: String, name: String) {
    var href = ""
    var date = ""
    var name = ""

    init {
        this.href = href
        this.date = date
        this.name = name
    }
}