package com.sungbin.com.myreadingmanga.dto

class MangaFavItem {
    var link: String? = null
    var name: String? = null

    constructor() {}
    constructor(link: String?, name: String?) {
        this.link = link
        this.name = name
    }

}