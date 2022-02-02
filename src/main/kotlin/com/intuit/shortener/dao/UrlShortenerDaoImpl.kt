package com.intuit.shortener.dao

import com.intuit.shortener.domain.Url
import com.intuit.shortener.mapper.UrlMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class UrlShortenerDaoImpl: UrlShortenerDao {

    @Autowired
    lateinit var urlMapper: UrlMapper

    override fun addUrl(url: Url) {
        urlMapper.addUrl(url)
    }

    override fun addHash(id: Int, hash: String) {
        return urlMapper.addHash(id, hash)
    }

    override fun getUrl(id: Int): String {
        return urlMapper.getUrl(id)
    }


}