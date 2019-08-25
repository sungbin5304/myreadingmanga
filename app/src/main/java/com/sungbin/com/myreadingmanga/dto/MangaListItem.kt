package com.sungbin.com.myreadingmanga.dto

class MangaListItem {
    var link: String? = null
    var name: String? = null
    var thumbnail: String? = null

    constructor() {}
    constructor(link: String?, name: String?, thumbnail: String?) {
        this.link = link
        this.name = name
        this.thumbnail = thumbnail
    }

}