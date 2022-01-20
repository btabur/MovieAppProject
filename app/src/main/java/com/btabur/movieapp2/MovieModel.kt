package com.btabur.movieapp2

class MovieModel {
    var id: String? = null
    var title: String? = null
    var description: String? = null
    var img: String? = null
    var date: String? = null
    var vote_avrage: String? = null

    constructor(id: String?) {
        this.id = id
    }

    constructor(
        id: String?,
        title: String?,
        description: String?,
        img: String?,
        date: String?,
        vote_avrage: String?
    ) {
        this.id = id
        this.title = title
        this.description = description
        this.img = img
        this.date = date
        this.vote_avrage = vote_avrage
    }

    constructor(
        title: String?,
        description: String?,
        img: String?,
        date: String?,
        vote_avrage: String?
    ) {
        this.title = title
        this.description = description
        this.img = img
        this.date = date
        this.vote_avrage = vote_avrage
    }

    constructor() {}
}