package com.intuit.shortener.service

import com.intuit.shortener.dao.DomainDao
import com.intuit.shortener.dao.DomainImpl
import com.intuit.shortener.dao.UrlShortenerDao
import com.intuit.shortener.dao.UrlShortenerDaoImpl
import com.intuit.shortener.domain.AddDomainRequest
import com.intuit.shortener.domain.Domains
import com.intuit.shortener.domain.Url
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UrlService {

    @Autowired
    lateinit var domainDao: DomainImpl

    @Autowired
    lateinit var urlShortenerDao: UrlShortenerDaoImpl

    fun addDomain( addDomainRequest: AddDomainRequest){
        domainDao.add(addDomainRequest)
    }

    fun getDomain(username: String): List<Domains> {
        return domainDao.get(username)
    }

    fun addUrl(url: Url): Url{
        urlShortenerDao.addUrl(url)
        return url
    }

    fun addHash(id: Int, hash: String){
        urlShortenerDao.addHash(id, hash)
    }

    fun getUrl(id: Int): String {
        return urlShortenerDao.getUrl(id)
    }

}