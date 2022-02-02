package com.intuit.shortener.dao

import com.intuit.shortener.domain.Url

interface UrlShortenerDao {
    fun addUrl(url: Url)
    fun addHash(id: Int, hash: String)
    fun getUrl(id: Int): String
}